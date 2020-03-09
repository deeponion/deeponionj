/*
 * Copyright 2013 Google Inc.
 * Copyright 2015 Andreas Schildbach
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bitcoinj.params;

import org.bitcoinj.core.*;
import org.bitcoinj.net.discovery.*;

import java.net.*;

import static com.google.common.base.Preconditions.*;

/**
 * Parameters for the main production network on which people trade goods and services.
 */
public class MainNetParams extends AbstractBitcoinNetParams {
    public static final int MAINNET_MAJORITY_WINDOW = 1000;
    public static final int MAINNET_MAJORITY_REJECT_BLOCK_OUTDATED = 950;
    public static final int MAINNET_MAJORITY_ENFORCE_BLOCK_UPGRADE = 750;

    public MainNetParams() {
        super();
        interval = INTERVAL;
        targetTimespan = TARGET_TIMESPAN;
        maxTarget = Utils.decodeCompactBits(0x1e0fffffL);
        dumpedPrivateKeyHeader = 159;
        addressHeader = 31;
        p2shHeader = 78;
        segwitAddressHrp = "dpn";
        port = 17570;
        packetMagic = 0xd1f1dbf2L;
        bip32HeaderP2PKHpub = 0x0488b21e; // The 4 byte header that serializes in base58 to "xpub".
        bip32HeaderP2PKHpriv = 0x0488ade4; // The 4 byte header that serializes in base58 to "xprv"
        bip32HeaderP2WPKHpub = 0x04b24746; // The 4 byte header that serializes in base58 to "zpub".
        bip32HeaderP2WPKHpriv = 0x04b2430c; // The 4 byte header that serializes in base58 to "zprv"

        majorityEnforceBlockUpgrade = MAINNET_MAJORITY_ENFORCE_BLOCK_UPGRADE;
        majorityRejectBlockOutdated = MAINNET_MAJORITY_REJECT_BLOCK_OUTDATED;
        majorityWindow = MAINNET_MAJORITY_WINDOW;

        genesisBlock.setDifficultyTarget(0x1e0fffffL);
        genesisBlock.setTime(1499843027L);
        genesisBlock.setNonce(3832541L);
        id = ID_MAINNET;
        subsidyDecreaseBlockCount = 131400;
        spendableCoinbaseDepth = 40;
        String genesisHash = genesisBlock.getHashAsString();
        checkState(genesisHash.equals("000004e29458ef4f2e0abab544737b07344e6ff13718f7c2d12926166db07b5e"), genesisHash);

        // This contains (at a minimum) the blocks which are not BIP30 compliant. BIP30 changed how duplicate
        // transactions are handled. Duplicated transactions could occur in the case where a coinbase had the same
        // extraNonce and the same outputs but appeared at different heights, and greatly complicated re-org handling.
        // Having these here simplifies block connection logic considerably.
        checkpoints.put(1000, Sha256Hash.wrap("0000000000850dc77a9601bc78cc6404bf24923475194c392accdb176c8e74da"));
        checkpoints.put(10000, Sha256Hash.wrap("c304f427bac3bef29e3faf8fe17d483a1e61d3a1749f9a0ac7ece4c1e544bbaa"));
        checkpoints.put(50008, Sha256Hash.wrap("00000000000a70b080e12cab200125ecffeebfae7c7fe7c914311a2b1056cf5b"));
        checkpoints.put(100000, Sha256Hash.wrap("00000000000338c044ce7a397242372cbc5d71ad7bad08c88089d0011de30f83"));
        checkpoints.put(150006, Sha256Hash.wrap("000000000000b5655b0eb5215167d4e18df3c5a43f67494f0385f87be6956114"));
        checkpoints.put(200830, Sha256Hash.wrap("000000000000dc867b2fad981c9e4ead671ebe1e0069ba7c9f43ba5a8ef67a07"));
        checkpoints.put(250008, Sha256Hash.wrap("00000000000069afef0ee5775301da6815092c4bd1a45928e33da5a894e55e00"));
        checkpoints.put(300836, Sha256Hash.wrap("00000000000047d897f3eac5fcacc831f0f77dfbf70222467ee2360dcbc4b266"));
        checkpoints.put(350003, Sha256Hash.wrap("0000000000001e6123edc0c5c8ca04ccee16aeb23455d02f12ba8d636516ee52"));
        checkpoints.put(400002, Sha256Hash.wrap("0000000000002945e035c7fabd433932269af99f5c033a6c9cea229b67751ba1"));
        checkpoints.put(450000, Sha256Hash.wrap("0000000000009bee8de0c52e5f4ea17a69e044b39718b62824eb67768ad18f4e"));
        checkpoints.put(500001, Sha256Hash.wrap("00000000000021a4ea50e863c7f9c3ab7a9ef1b452e498a0c5369a03fe224aec"));
        checkpoints.put(550004, Sha256Hash.wrap("0000000000008784d0e68a519407498b08d0b56b23726e4bfcc0843017d7d011"));
        checkpoints.put(600014, Sha256Hash.wrap("000000000000150feaa5a9669a7b58589333c8195770b675f873fc236cb9f6ce"));
        checkpoints.put(650002, Sha256Hash.wrap("000000000000007bc8502ddd5a6f0ce8fbf499a9c189ba4f6a42c6e3a7698b0f"));
        checkpoints.put(700006, Sha256Hash.wrap("00000000000052dd063e278fc0d3c95ebcfaa646215c350b7b8f505d6dc7e331"));
        checkpoints.put(750001, Sha256Hash.wrap("000000000000792fb98a1a8461e50a910543d90b712b2303e49779ee94238b27"));
        checkpoints.put(800002, Sha256Hash.wrap("000000000000b799930f12a319238e283590d7480ac5ca711679548c33e8ebf1"));
        checkpoints.put(850000, Sha256Hash.wrap("0000000000019d6b3b3a832567200db8b548f445ba78da7cc6c2fbe8e305c693"));
        checkpoints.put(900012, Sha256Hash.wrap("00000000000027b3afa207c5cb8c3e4232129273fa020b9a463d567a01f726cd"));
        checkpoints.put(950005, Sha256Hash.wrap("00000000000514c239cbb998a2d7b2a503b57d86b4739f8285fa05ef13fcabf2"));
        checkpoints.put(1000006, Sha256Hash.wrap("000000000005283dc62422cca1f4d09eaf220e41d9812ac7f659f4b7cb2c65c2"));
        checkpoints.put(1050000, Sha256Hash.wrap("000000000001ccf45545c4d685142d4ac5ca2f485b2f8d8c5e023de1d2f13228"));
        checkpoints.put(1100013, Sha256Hash.wrap("0000000000008d7041a212b8ca38e71732b3dcbed3075ed01959a82886a89d02"));
        checkpoints.put(1118505, Sha256Hash.wrap("000000000003c8328fbeafeb99a58030ab31d6ba52e17e111abd615d8cd8c6b9"));

        dnsSeeds = new String[] {
                "r4kyfixzyw3dbav2.onion",
                "zguaqny7wgap5zcj.onion",
                "stpyoqtxqpfganqy.onion",
                "xh7h73isdp4lls74.onion",
                "lofqzmalkpgiicfz.onion",
                "f2vozvjm7h2g3kkf.onion",
                "2gezzwzholh73zrt.onion",
                "4hcgtqhqvozduscx.onion",
                "wtipdviwjapfq7sj.onion",
                "nvhfgyrnqbt7rfnf.onion",
                "aafnohdwjnuxgwlp.onion",
                "yj7qrhx3agk5pmwq.onion"
        };
        httpSeeds = new HttpDiscovery.Details[] {};

        // These are in big-endian format, which is what the SeedPeers code expects.
        // Updated Apr. 11th 2019
        addrSeeds = new int[] {};
    }

    private static MainNetParams instance;
    public static synchronized MainNetParams get() {
        if (instance == null) {
            instance = new MainNetParams();
        }
        return instance;
    }

    @Override
    public String getPaymentProtocolId() {
        return PAYMENT_PROTOCOL_ID_MAINNET;
    }
}
