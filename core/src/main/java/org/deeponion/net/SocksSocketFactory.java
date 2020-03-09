package org.deeponion.net;

import com.google.common.base.Charsets;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.net.SocketFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class SocksSocketFactory extends SocketFactory {
    static final int READ_TIMEOUT_MILLISECONDS = 60000;
    static final int CONNECT_TIMEOUT_MILLISECONDS = 60000;
    private final String proxyAddress;
    private final int proxyPort;

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
        outputStream.write((byte)0x05); // SOCKS Version
        outputStream.write((byte)0x02); // NAUTH Supported Auth methods
        outputStream.write((byte)0x00); // No authentication
        outputStream.write((byte)0x02); // User/Pass

        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
        byte socksVersion = inputStream.readByte();
        byte cuath = inputStream.readByte();
        if (socksVersion != (byte)0x05 || cuath != (byte)0x02) {
            socket.close();
            throw new IOException("SOCKS5 connect failed, got " + socksVersion + " - " + cuath +
                    ", but expected 0x05 - 0x00:, networkHost= " + host + ", networkPort = " + port
                    + ", socksHost=" + socket.getInetAddress().toString());
        }

        outputStream.write((byte)0x01); // Current version of username/password authentication
        outputStream.write((byte)0x01); // ID Len
        outputStream.write((byte)0x30); // ID
        outputStream.write((byte)0x01); // Password Len
        outputStream.write((byte)0x30); // Password

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
        outputStream.write(host.getBytes(Charsets.UTF_8));
        outputStream.write((byte)0x00); // Null terminate host
        outputStream.writeShort((short)port);

        byte b1 = inputStream.readByte();
        byte b2 = inputStream.readByte();
        byte b3 = inputStream.readByte();
        byte b4 = inputStream.readByte();
        int i1 = inputStream.readInt();
        short s1 = inputStream.readShort();
        return socket;

    }
}
