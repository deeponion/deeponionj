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
import org.bitcoinj.params.MainNetParams;

import java.net.InetSocketAddress;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * This class implements various types of discovery over Socks5,
 * which can be enabled/disabled via constructor flag.
 */
public class SocksMultiDiscovery implements PeerDiscovery {

    private final ArrayList<PeerDiscovery> discoveryList = new ArrayList<>();

    /**
     * Supports finding peers by hostname over a socks5 proxy.
     *
     * @param params param to be used for seed and port information.
         */
    public SocksMultiDiscovery(NetworkParameters params) {
        discoveryList.add(new Socks5SeedOnionDiscovery(params));
    }

    /**
     * Returns an array containing all the Bitcoin nodes that have been discovered.
     */
    @Override
    public InetSocketAddress[] getPeers(long services, long timeoutValue, TimeUnit timeoutUnit) throws PeerDiscoveryException {
        ArrayList<InetSocketAddress> list = new ArrayList<>();
        for (PeerDiscovery discovery : discoveryList) {
            list.addAll(Arrays.asList(discovery.getPeers(services, timeoutValue, timeoutUnit)));
        }

        return list.toArray(new InetSocketAddress[list.size()]);
    }

    @Override
    public void shutdown() {
    }
}
