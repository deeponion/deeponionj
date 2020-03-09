package org.deeponion.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import static org.deeponion.net.SocksSocketFactory.CONNECT_TIMEOUT_MILLISECONDS;

public class SocksSocket extends Socket {

    private final String proxyAddress;
    private final int proxyPort;

    public SocksSocket(String proxyAddress, int proxyPort) {
        this.proxyAddress = proxyAddress;
        this.proxyPort = proxyPort;
    }

    @Override
    public void connect(SocketAddress endpoint) throws IOException {
        SocketAddress socksAddress = new InetSocketAddress(proxyAddress, proxyPort);
        super.connect(socksAddress);
        SocksSocketFactory.initProxy(
                this,
                ((InetSocketAddress)endpoint).getHostString(),
                ((InetSocketAddress)endpoint).getPort()
        );
    }

    @Override
    public void connect(SocketAddress endpoint, int timeout) throws IOException {
        SocketAddress socksAddress = new InetSocketAddress(proxyAddress, proxyPort);
        super.connect(socksAddress, timeout);
        SocksSocketFactory.initProxy(
                this,
                ((InetSocketAddress)endpoint).getHostString(),
                ((InetSocketAddress)endpoint).getPort()
        );
    }
}
