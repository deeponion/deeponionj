/*
 * This file is part of Bisq.
 *
 * Bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * Bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package org.bitcoinj.net.discovery;

import org.bitcoinj.core.NetworkParameters;

import java.net.InetSocketAddress;

import java.util.concurrent.TimeUnit;

/**
 * Socks5SeedOnionDiscovery provides a list of known Bitcoin .onion seeds.
 * These are nodes running as hidden services on the Tor network.
 */
public class Socks5SeedOnionDiscovery implements PeerDiscovery {
    private InetSocketAddress[] seedAddrs;

    /**
     * Supports finding peers by hostname over a socks5 proxy.
     *
     * @param params param to be used for seed and port information.
     */
    public Socks5SeedOnionDiscovery(NetworkParameters params) {
        // We do this because NetworkParameters does not contain any .onion
        // seeds.  Perhaps someday...
        String[] seedAddresses = {};
        seedAddresses = params.getDnsSeeds();
        this.seedAddrs = convertAddrsString(seedAddresses, params.getPort());
    }

    /**
     * Returns an array containing all the Bitcoin nodes within the list.
     */
    @Override
    public InetSocketAddress[] getPeers(long services, long timeoutValue, TimeUnit timeoutUnit) throws PeerDiscoveryException {
        if (services != 0)
            throw new PeerDiscoveryException("DNS seeds cannot filter by services: " + services);
        return seedAddrs;
    }

    /**
     * Converts an array of hostnames to array of unresolved InetSocketAddress
     */
    private InetSocketAddress[] convertAddrsString(String[] addrs, int port) {
        InetSocketAddress[] list = new InetSocketAddress[addrs.length];
        for (int i = 0; i < addrs.length; i++) {
            list[i] = InetSocketAddress.createUnresolved(addrs[i], port);
        }
        return list;
    }

    @Override
    public void shutdown() {
        //TODO should we add a DnsLookupTor.shutdown() ?
    }
}
