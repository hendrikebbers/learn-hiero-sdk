package org.hiero.sdk.simple.sample;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.PublicKey;
import io.github.cdimascio.dotenv.Dotenv;
import org.hiero.sdk.simple.HieroClient;
import org.hiero.sdk.simple.network.Account;
import org.hiero.sdk.simple.transactions.AccountCreateResponse;
import org.hiero.sdk.simple.transactions.AccountCreateTransaction;

public class Sample {

    public static void main(String[] args) throws Exception {
        final Account operatorAccount = createOperatorAccount();
        final HieroClient hieroClient = HieroClient.create(operatorAccount, "hedera-testnet");

        final PublicKey publicKeyForNewAccount = PrivateKey.generateED25519().getPublicKey();

        final AccountCreateResponse response = new AccountCreateTransaction()
                .withKey(publicKeyForNewAccount)
                .withFee(Hbar.from(10))
                .withMemo("Sample account creation")
                .withAccountMemo("Sample account memo")
                .withInitialBalance(Hbar.from(2))
                .freezeTransaction(hieroClient)
                .executeAndWait();

        System.out.println("Transaction executed!");
    }

    private static Account createOperatorAccount() {
        // Load environment variables from .env file
        final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        final AccountId operatorAccountId = AccountId.fromString(dotenv.get("OPERATOR_ACCOUNT_ID"));
        final PrivateKey operatorPrivateKey = PrivateKey.fromString(dotenv.get("OPERATOR_PRIVATE_KEY"));
        return Account.of(operatorAccountId, operatorPrivateKey);
    }
}
