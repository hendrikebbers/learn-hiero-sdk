package org.hiero.sdk.simple.sample;

import org.hiero.sdk.simple.HieroClient;
import org.hiero.sdk.simple.network.Account;
import org.hiero.sdk.simple.network.AccountId;
import org.hiero.sdk.simple.network.Hbar;
import org.hiero.sdk.simple.network.keys.KeyAlgorithm;
import org.hiero.sdk.simple.network.keys.KeyPair;
import org.hiero.sdk.simple.network.keys.PrivateKey;
import org.hiero.sdk.simple.network.keys.PublicKey;
import org.hiero.sdk.simple.transactions.AccountCreateTransaction;

public class Sample2 {

    public static void main(String[] args) throws Exception {
        final Account operatorAccount = createOperatorAccount();
        final HieroClient hieroClient = HieroClient.create(operatorAccount, "hiero-test");
        final PublicKey publicKeyForNewAccount = KeyPair.generate(KeyAlgorithm.ED25519).publicKey();

        new AccountCreateTransaction().
                withKey(publicKeyForNewAccount)
                .withInitialBalance(Hbar.of(2))
                .freezeTransaction(hieroClient)
                .sendAndWait();


    }

    private static Account createOperatorAccount() {
        final AccountId operatorAccountId = AccountId.from("0.0.1001");
        final PrivateKey operatorPrivateKey = PrivateKey.from(
                "3030020100300706052b8104000a0422042075f2484bc16882f1158453ac3afdb5d1620267ce45b2871122bd8c06af5fb396");
        return Account.of(operatorAccountId, operatorPrivateKey);
    }
}
