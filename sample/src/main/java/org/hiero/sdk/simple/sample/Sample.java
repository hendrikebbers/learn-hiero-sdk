package org.hiero.sdk.simple.sample;

import static java.lang.System.Logger.Level.INFO;

import io.github.cdimascio.dotenv.Dotenv;
import org.hiero.sdk.simple.HieroClient;
import org.hiero.sdk.simple.network.Account;
import org.hiero.sdk.simple.network.AccountId;
import org.hiero.sdk.simple.network.Hbar;
import org.hiero.sdk.simple.network.keys.KeyAlgorithm;
import org.hiero.sdk.simple.network.keys.PrivateKey;
import org.hiero.sdk.simple.network.keys.PublicKey;
import org.hiero.sdk.simple.transactions.AccountCreateResponse;
import org.hiero.sdk.simple.transactions.AccountCreateTransaction;

public class Sample {

    private final static System.Logger log = System.getLogger(Sample.class.getName());

    public static void main(String[] args) throws Exception {
        final Account operatorAccount = createOperatorAccount();
        final HieroClient hieroClient = HieroClient.create(operatorAccount, "hedera-testnet");

        final PublicKey publicKeyForNewAccount = PrivateKey.generate(KeyAlgorithm.ED25519).createPublicKey();

        final AccountCreateResponse response = new AccountCreateTransaction()
                .withKey(publicKeyForNewAccount)
                .withFee(Hbar.of(10))
                .withMemo("Sample account creation")
                .withAccountMemo("Sample account memo")
                .withInitialBalance(Hbar.of(2))
                .freezeTransaction(hieroClient)
                .executeAndWait();

        log.log(INFO, "Transaction {0} executed", response.transactionId());
    }

    private static Account createOperatorAccount() {
        // Load environment variables from .env file
        final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        final AccountId operatorAccountId = AccountId.fromString(dotenv.get("OPERATOR_ACCOUNT_ID"));
        final PrivateKey operatorPrivateKey = PrivateKey.fromString(dotenv.get("OPERATOR_PRIVATE_KEY"));
        return Account.of(operatorAccountId, operatorPrivateKey);
    }
}
