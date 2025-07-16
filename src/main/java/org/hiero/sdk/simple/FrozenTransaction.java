package org.hiero.sdk.simple;

import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.PublicKey;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.UnaryOperator;
import org.hiero.sdk.simple.network.Account;
import org.jspecify.annotations.NonNull;

public interface FrozenTransaction<R extends TransactionResponse> {

    @NonNull
    default FrozenTransaction<R> sign(Account operatorAccount) {
        Objects.requireNonNull(operatorAccount, "operatorAccount must not be null");
        return sign(operatorAccount.privateKey());
    }

    @NonNull
    default FrozenTransaction<R> sign(@NonNull final PrivateKey privateKey) {
        Objects.requireNonNull(privateKey, "privateKey must not be null");
        return sign(privateKey.getPublicKey(), privateKey::sign);
    }

    @NonNull
    FrozenTransaction<R> sign(@NonNull PublicKey publicKey, @NonNull UnaryOperator<byte[]> transactionSigner);

    CompletableFuture<R> execute();

    R executeAndWait() throws ExecutionException, InterruptedException;

}
