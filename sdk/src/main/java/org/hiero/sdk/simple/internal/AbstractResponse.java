package org.hiero.sdk.simple.internal;

import com.hedera.hashgraph.sdk.proto.TransactionReceipt;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiFunction;
import org.hiero.sdk.simple.HieroClient;
import org.hiero.sdk.simple.Receipt;
import org.hiero.sdk.simple.Response;
import org.hiero.sdk.simple.network.TransactionId;
import org.jspecify.annotations.NonNull;

public abstract class AbstractResponse<R extends Receipt> implements Response<R> {

    private final HieroClient client;

    private final TransactionId transactionId;

    private final BiFunction<TransactionId, TransactionReceipt, R> receiptFactory;

    public AbstractResponse(@NonNull final HieroClient client,
            @NonNull final TransactionId transactionId,
            @NonNull final BiFunction<TransactionId, TransactionReceipt, R> receiptFactory) {
        this.client = Objects.requireNonNull(client, "client must not be null");
        this.transactionId = Objects.requireNonNull(transactionId, "transactionId must not be null");
        this.receiptFactory = Objects.requireNonNull(receiptFactory, "receiptFactory must not be null");
    }

    @Override
    public TransactionId transactionId() {
        return transactionId;
    }

    @Override
    public CompletableFuture<R> queryReceipt() {
        return client.queryTransactionReceipt(transactionId, receiptFactory);
    }

    @Override
    public R queryReceiptAndWait()
            throws InterruptedException, ExecutionException, TimeoutException {
        final long defaultTimeoutInMs = client.getDefaultTimeoutInMs();
        return queryReceiptAndWait(defaultTimeoutInMs, TimeUnit.MILLISECONDS);
    }
}
