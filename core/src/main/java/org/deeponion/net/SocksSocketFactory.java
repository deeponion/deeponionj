package org.deeponion.net;

import com.google.common.base.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.net.SocketFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class SocksSocketFactory extends SocketFactory {
    private static final Logger log = LoggerFactory.getLogger(SocksSocketFactory.class);
    static final int READ_TIMEOUT_MILLISECONDS = 60000;
    static final int CONNECT_TIMEOUT_MILLISECONDS = 60000;
    private final String proxyAddress;
    private final int proxyPort;

    private static String getError(byte errorCode) {
        switch(errorCode) {
            case 0x00:
                return "Tor succeeded";
            case 0x01:
                return "Tor general error";
            case 0x02:
                return "Tor not allowed";
            case 0x03:
                return "Tor network is unreachable";
            case 0x04:
                return "Tor host is unreachable";
            case 0x05:
                return "Tor connection refused";
            case 0x06:
                return "Tor TTL expired";
            case 0x07:
                return "Tor command not supported";
            case 0x08:
                return "Tor address type not supported";
            default:
                return "Unknown Tor error code.";
        }
    }

    public SocksSocketFactory(@Nonnull String proxyAddress, int proxyPort) {
        this.proxyAddress = proxyAddress;
        this.proxyPort = proxyPort;
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return createSocket(proxyAddress, proxyPort, host, port, null, -1, CONNECT_TIMEOUT_MILLISECONDS);
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
        return createSocket(proxyAddress, proxyPort, host, port, localHost, localPort, CONNECT_TIMEOUT_MILLISECONDS);
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        return createSocket(proxyAddress, proxyPort, host.getHostAddress(), port, null, -1, CONNECT_TIMEOUT_MILLISECONDS);
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        return createSocket(proxyAddress, proxyPort, address.getHostAddress(), port, localAddress, localPort, CONNECT_TIMEOUT_MILLISECONDS);
    }

    @Override
    public Socket createSocket() throws IOException {
        return new SocksSocket(proxyAddress, proxyPort);
    }

    public static Socket createSocket(String proxyAddress, int proxyPort, String host, int port, @Nullable InetAddress localAddress, int localPort, int timeout) throws IOException {
        Socket socket;
        if(localAddress != null) {
            socket = new Socket(proxyAddress, proxyPort, localAddress, localPort);
        } else {
            socket = new Socket();
            SocketAddress socksAddress = new InetSocketAddress(proxyAddress, proxyPort);
            socket.connect(socksAddress, timeout);
        }
        initProxy(socket, host, port);
        return socket;
    }
    public static Socket initProxy(Socket socket ,String host, int port) throws IOException {
        socket.setSoTimeout(READ_TIMEOUT_MILLISECONDS);

        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        byte[] greeting = new byte[4];
        greeting[0] = 0x05; // SOCKS Version
        greeting[1] = 0x02; // NAUTH Supported Auth methods
        greeting[2] = 0x00; // No authentication
        greeting[3] = 0x02; // User/Pass
        outputStream.write(greeting);

        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
        byte socksVersion = inputStream.readByte();
        byte cuath = inputStream.readByte();
        if (socksVersion != (byte)0x05 || cuath != (byte)0x02) {
            socket.close();
            throw new IOException("SOCKS5 connect failed, got " + socksVersion + " - " + cuath +
                    ", but expected 0x05 - 0x00:, networkHost= " + host + ", networkPort = " + port
                    + ", socksHost=" + socket.getInetAddress().toString());
        }

        byte[] authtication = new byte[5];
        authtication[0] = 0x01; // Current version of username/password authentication
        authtication[1] = 0x01; // ID Len
        authtication[2] = 0x30; // ID
        authtication[3] = 0x01; // Password Len
        authtication[4] = 0x30; // Password
        outputStream.write(authtication);

        byte ver = inputStream.readByte();
        byte status = inputStream.readByte();
        if (ver != (byte)0x01 || status != (byte)0x00) {
            socket.close();
            throw new IOException("SOCKS5 authenticate failed, got " + ver + " - " + status +
                    ", but expected 0x01 - 0x00:, networkHost= " + host + ", networkPort = " + port
                    + ", socksHost=" + socket.getInetAddress().toString());
        }

        outputStream.write((byte)0x05); // SOCKS Version
        outputStream.write((byte)0x01); // TCP Stream
        outputStream.write((byte)0x00); // RSV
        outputStream.write((byte)0x03); // Domain Name
        outputStream.write((byte)host.length()); // Domain Length
        outputStream.write(host.getBytes(Charsets.UTF_8));
        outputStream.writeShort((short)port);

        byte b1 = inputStream.readByte();
        byte b2 = inputStream.readByte();
        if(b2 == 0x00) {
            log.info("SOCKS5 Proxy to " + host + " connected.");
            return socket;
        } else {
            socket.close();
            throw new IOException("SOCKS5 proxy failed: " + getError(b2));
        }
    }
}
