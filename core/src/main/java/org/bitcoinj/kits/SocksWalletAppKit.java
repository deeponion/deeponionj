package org.bitcoinj.kits;

import org.bitcoinj.core.Context;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.net.BlockingClientManager;
import org.bitcoinj.script.Script;
import org.bitcoinj.wallet.KeyChainGroupStructure;
import org.deeponion.net.SocksSocketFactory;

import javax.annotation.Nullable;
import java.io.File;

public class SocksWalletAppKit extends WalletAppKit {
    final SocksSocketFactory socksSocketFactory;
    public SocksWalletAppKit(NetworkParameters params, File directory, String filePrefix, SocksSocketFactory socksSocketFactory) {
        super(params, directory, filePrefix);
        this.socksSocketFactory = socksSocketFactory;
    }

    public SocksWalletAppKit(NetworkParameters params, Script.ScriptType preferredOutputScriptType, @Nullable KeyChainGroupStructure structure, File directory, String filePrefix, SocksSocketFactory socksSocketFactory) {
        super(params, preferredOutputScriptType, structure, directory, filePrefix);
        this.socksSocketFactory = socksSocketFactory;
    }

    public SocksWalletAppKit(Context context, Script.ScriptType preferredOutputScriptType, @Nullable KeyChainGroupStructure structure, File directory, String filePrefix, SocksSocketFactory socksSocketFactory) {
        super(context, preferredOutputScriptType, structure, directory, filePrefix);
        this.socksSocketFactory = socksSocketFactory;
    }

    @Override
    protected PeerGroup createPeerGroup() {
        return new PeerGroup(params, vChain, new BlockingClientManager(socksSocketFactory));
    }
}
