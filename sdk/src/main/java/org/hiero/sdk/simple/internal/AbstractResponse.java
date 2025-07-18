package org.hiero.sdk.simple.internal;

import com.hedera.hashgraph.sdk.proto.Query;
import com.hedera.hashgraph.sdk.proto.QueryHeader;
import com.hedera.hashgraph.sdk.proto.ResponseType;
import com.hedera.hashgraph.sdk.proto.TransactionGetReceiptQuery;
import io.grpc.MethodDescriptor;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.hiero.sdk.simple.HieroClient;
import org.hiero.sdk.simple.Receipt;
import org.hiero.sdk.simple.Response;
import org.hiero.sdk.simple.internal.grpc.CryptoServiceGrpc;
import org.hiero.sdk.simple.internal.util.ProtobufUtil;
import org.hiero.sdk.simple.network.TransactionId;
import org.jspecify.annotations.NonNull;

public abstract class AbstractResponse implements Response {

    private final HieroClient client;

    private final TransactionId transactionId;

    public AbstractResponse(@NonNull final HieroClient client, @NonNull final TransactionId transactionId) {
        this.client = Objects.requireNonNull(client, "client must not be null");
        this.transactionId = Objects.requireNonNull(transactionId, "transactionId must not be null");
    }

    @Override
    public TransactionId transactionId() {
        return transactionId;
    }

    @Override
    public CompletableFuture<Receipt> getReceipt() {
        final QueryHeader header = QueryHeader.newBuilder()
                .setResponseType(ResponseType.ANSWER_ONLY)
                .build();
        final TransactionGetReceiptQuery typesQuery = TransactionGetReceiptQuery.newBuilder()
                .setHeader(header)
                .setTransactionID(ProtobufUtil.toProtobuf(transactionId))
                .build();
        Query query = Query.newBuilder()
                .setTransactionGetReceipt(typesQuery)
                .build();
        MethodDescriptor<Query, com.hedera.hashgraph.sdk.proto.Response> methodDescriptor = CryptoServiceGrpc.getGetTransactionReceiptsMethod();
        return client.getGrpcClient().call(methodDescriptor, query).handle((response, throwable) -> {
            if (throwable != null) {
                throw new RuntimeException("Transaction execution failed", throwable);
            }
            throw new RuntimeException("Not implemented");
        });
    }

    @Override
    public Receipt getReceiptAndWait()
            throws InterruptedException, ExecutionException, TimeoutException {
        final long defaultTimeoutInMs = client.getDefaultTimeoutInMs();
        return getReceiptAndWait(defaultTimeoutInMs, TimeUnit.MILLISECONDS);
    }
}
