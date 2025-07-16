package org.hiero.sdk.simple.sample;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrivateKey;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.concurrent.CompletableFuture;
import org.hiero.sdk.simple.FrozenTransaction;
import org.hiero.sdk.simple.HieroClient;
import org.hiero.sdk.simple.network.Account;
import org.hiero.sdk.simple.transactions.AccountCreateResponse;
import org.hiero.sdk.simple.transactions.AccountCreateTransaction;

public class Sample {

    public static void main(String[] args) throws Exception {
        final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        final AccountId operatorAccountId = AccountId.fromString(dotenv.get("OPERATOR_ACCOUNT_ID"));
        final PrivateKey operatorPrivateKey = PrivateKey.fromString(dotenv.get("OPERATOR_PRIVATE_KEY"));
        final Account operatorAccount = Account.of(operatorAccountId, operatorPrivateKey);
        final HieroClient hieroClient = HieroClient.create(operatorAccount, "hedera-testnet");

        final AccountCreateTransaction accountCreateTransaction = new AccountCreateTransaction()
                .withFee(Hbar.from(10))
                .withMemo("Sample account creation")
                .withAccountMemo("Sample account memo")
                .withInitialBalance(Hbar.from(2));

        final FrozenTransaction<AccountCreateResponse> frozenTransaction = accountCreateTransaction.freezeTransaction(
                hieroClient);
        frozenTransaction.sign(operatorAccount);

        final AccountCreateResponse response = frozenTransaction.executeAndWait();

        //Or 100 fluent
        final AccountCreateResponse response2 = new AccountCreateTransaction()
                .withFee(Hbar.from(10))
                .withMemo("Sample account creation")
                .withAccountMemo("Sample account memo")
                .withInitialBalance(Hbar.from(2))
                .freezeTransaction(hieroClient)
                .sign(operatorAccount)
                .executeAndWait();

        final CompletableFuture<AccountCreateResponse> response3 = new AccountCreateTransaction()
                .withFee(Hbar.from(10))
                .withMemo("Sample account creation")
                .withAccountMemo("Sample account memo")
                .withInitialBalance(Hbar.from(2))
                .freezeTransaction(hieroClient)
                .sign(operatorAccount)
                .execute();
    }
}
