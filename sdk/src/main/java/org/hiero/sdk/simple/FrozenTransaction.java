package org.hiero.sdk.simple;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.UnaryOperator;
import org.hiero.sdk.simple.network.keys.PrivateKey;
import org.hiero.sdk.simple.network.keys.PublicKey;
import org.jspecify.annotations.NonNull;

/**
 * Represents a frozen transaction that can be signed and sent to the network.
 *
 * @param <T> the type of the transaction
 * @param <R> the type of the response
 */
public interface FrozenTransaction<T extends Transaction, R extends Response> {

    /**
     * Signs the transaction with the given private key.
     *
     * @param privateKey the private key to sign the transaction with
     * @return a new {@link FrozenTransaction} instance with the signature applied
     * @throws NullPointerException if {@code privateKey} is null
     */
    @NonNull
    default FrozenTransaction<T, R> sign(@NonNull final PrivateKey privateKey) {
        Objects.requireNonNull(privateKey, "privateKey must not be null");
        return sign(privateKey.createPublicKey(), privateKey::sign);
    }

    /**
     * Signs the transaction with the given public key by using the given transaction signer function.
     *
     * @param publicKey         the public key to sign the transaction with
     * @param transactionSigner a function that takes a byte array and returns a signed byte array
     * @return a new {@link FrozenTransaction} instance with the signature applied
     * @throws NullPointerException if {@code publicKey} or {@code transactionSigner} is null
     */
    @NonNull
    FrozenTransaction<T, R> sign(@NonNull PublicKey publicKey, @NonNull UnaryOperator<byte[]> transactionSigner);

    /**
     * Sends the frozen transaction to the network asynchronously.
     *
     * @return a {@link CompletableFuture} that will complete with the response of the transaction
     */
    CompletableFuture<R> send();

    /**
     * Sends the frozen transaction to the network and waits for the response.
     *
     * @return the response of the transaction
     * @throws ExecutionException   if the transaction fails
     * @throws InterruptedException if the thread is interrupted while waiting
     * @throws TimeoutException     if the wait times out
     */
    default R sendAndWait(long timeout, TimeUnit unit)
            throws ExecutionException, InterruptedException, TimeoutException {
        Objects.requireNonNull(unit, "unit must not be null");
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout must be non-negative");
        }
        return send().get(timeout, unit);
    }

    /**
     * Sends the frozen transaction to the network and waits for the response with a default timeout.
     *
     * @return the response of the transaction
     * @throws ExecutionException   if the transaction fails
     * @throws InterruptedException if the thread is interrupted while waiting
     * @throws TimeoutException     if the wait times out
     */
    R sendAndWait() throws ExecutionException, InterruptedException, TimeoutException;

    /**
     * Unpacks the frozen transaction into its original transaction type. Each call to this method will return a new
     * instance.
     *
     * @return the original transaction
     */
    T unpack();
}
