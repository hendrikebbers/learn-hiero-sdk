package org.hiero.sdk.simple;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.hiero.sdk.simple.network.TransactionId;

public interface Response<R extends Receipt> {

    TransactionId transactionId();

    CompletableFuture<R> queryReceipt();

    default R queryReceiptAndWait(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        Objects.requireNonNull(unit, "unit must not be null");
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout must be non-negative");
        }
        return queryReceipt().get(timeout, unit);
    }

    R queryReceiptAndWait() throws InterruptedException, ExecutionException, TimeoutException;
}
