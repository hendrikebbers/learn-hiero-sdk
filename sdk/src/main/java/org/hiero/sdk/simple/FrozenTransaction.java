package org.hiero.sdk.simple;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.UnaryOperator;
import org.hiero.sdk.simple.network.keys.PrivateKey;
import org.hiero.sdk.simple.network.keys.PublicKey;
import org.jspecify.annotations.NonNull;

public interface FrozenTransaction<T extends Transaction, R extends Response> {

    @NonNull
    default FrozenTransaction<T, R> sign(@NonNull final PrivateKey privateKey) {
        Objects.requireNonNull(privateKey, "privateKey must not be null");
        return sign(privateKey.createPublicKey(), privateKey::sign);
    }

    @NonNull
    FrozenTransaction<T, R> sign(@NonNull PublicKey publicKey, @NonNull UnaryOperator<byte[]> transactionSigner);

    CompletableFuture<R> execute();

    R executeAndWait() throws ExecutionException, InterruptedException;

    T unpack();
}
