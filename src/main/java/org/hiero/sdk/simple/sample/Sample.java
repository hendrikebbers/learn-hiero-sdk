package org.hiero.sdk.simple.sample;

import com.hedera.hashgraph.sdk.Hbar;
import org.hiero.sdk.simple.HieroClient;
import org.hiero.sdk.simple.transactions.AccountCreateResponse;
import org.hiero.sdk.simple.transactions.AccountCreateTransaction;

public class Sample {

    public static void main(String[] args) throws Exception {
        HieroClient hieroClient = null;

        final AccountCreateResponse response = new AccountCreateTransaction()
                .withFee(Hbar.from(10))
                .withMemo("Sample account creation")
                .withAccountMemo("Sample account memo")
                .withInitialBalance(Hbar.from(2))
                .freezeSignExecuteAndWait(hieroClient);
    }
}
