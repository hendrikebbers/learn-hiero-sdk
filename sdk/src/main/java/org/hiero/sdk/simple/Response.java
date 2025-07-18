package org.hiero.sdk.simple;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.hiero.sdk.simple.network.TransactionId;

public interface Response {

    TransactionId transactionId();

    CompletableFuture<Receipt> getReceipt();

    default Receipt getReceiptAndWait(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        Objects.requireNonNull(unit, "unit must not be null");
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout must be non-negative");
        }
        return getReceipt().get(timeout, unit);
    }

    Receipt getReceiptAndWait() throws InterruptedException, ExecutionException, TimeoutException;
}
