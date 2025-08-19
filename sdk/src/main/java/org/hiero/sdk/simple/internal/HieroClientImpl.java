package org.hiero.sdk.simple.internal;

import com.hedera.hashgraph.sdk.proto.Query;
import com.hedera.hashgraph.sdk.proto.QueryHeader;
import com.hedera.hashgraph.sdk.proto.Response;
import com.hedera.hashgraph.sdk.proto.ResponseCodeEnum;
import com.hedera.hashgraph.sdk.proto.ResponseType;
import com.hedera.hashgraph.sdk.proto.TransactionGetReceiptQuery;
import com.hedera.hashgraph.sdk.proto.TransactionGetReceiptResponse;
import com.hedera.hashgraph.sdk.proto.TransactionReceipt;
import com.hedera.hashgraph.sdk.proto.TransactionRecord;
import io.grpc.MethodDescriptor;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import org.hiero.sdk.simple.HieroClient;
import org.hiero.sdk.simple.Receipt;
import org.hiero.sdk.simple.Record;
import org.hiero.sdk.simple.grpc.GrpcClient;
import org.hiero.sdk.simple.internal.grpc.GrpcClientImpl;
import org.hiero.sdk.simple.internal.grpc.GrpcMethodDescriptorFactory;
import org.hiero.sdk.simple.internal.util.ProtobufUtil;
import org.hiero.sdk.simple.network.Account;
import org.hiero.sdk.simple.network.TransactionId;
import org.hiero.sdk.simple.network.settings.NetworkSettings;
import org.jspecify.annotations.NonNull;

public final class HieroClientImpl implements HieroClient {

    private final Account operatorAccount;

    private final Executor executor;

    private final NetworkSettings networkSettings;

    public HieroClientImpl(@NonNull final Account operatorAccount, @NonNull final NetworkSettings networkSettings,
            @NonNull final Executor executor) {
        this.operatorAccount = Objects.requireNonNull(operatorAccount, "operatorAccount must not be null");
        this.networkSettings = Objects.requireNonNull(networkSettings, "networkSettings must not be null");
        this.executor = Objects.requireNonNull(executor, "executor must not be null");
    }

    @Override
    public @NonNull TransactionId generateTransactionId() {
        return TransactionId.generate(operatorAccount.accountId());
    }


    @Override
    public @NonNull <R extends Receipt> CompletableFuture<R> queryTransactionReceipt(
            @NonNull TransactionId transactionId, BiFunction<TransactionId, TransactionReceipt, R> receiptFactory) {
        Objects.requireNonNull(transactionId, "transactionId must not be null");
        final QueryHeader header = QueryHeader.newBuilder()
                .setResponseType(ResponseType.ANSWER_ONLY)
                .build();
        final TransactionGetReceiptQuery typesQuery = TransactionGetReceiptQuery.newBuilder()
                .setHeader(header)
                .setTransactionID(ProtobufUtil.toProtobuf(transactionId))
                .build();
        final Query query = Query.newBuilder()
                .setTransactionGetReceipt(typesQuery)
                .build();
        final MethodDescriptor<Query, Response> methodDescriptor = GrpcMethodDescriptorFactory.getOrCreateMethodDescriptor(
                "proto.CryptoService",
                "getTxRecordByTxID",
                com.hedera.hashgraph.sdk.proto.Query::getDefaultInstance,
                Response::getDefaultInstance);
        ;
        return getGrpcClient().call(methodDescriptor, query).handle((response, throwable) -> {
            if (throwable != null) {
                throw new RuntimeException("Transaction execution failed", throwable);
            }
            if (response == null) {
                throw new IllegalStateException("Received null response from the server");
            }
            if (response.getTransactionGetReceipt() == null) {
                throw new IllegalStateException("Response does not contain TransactionGetReceipt");
            }
            final TransactionGetReceiptResponse transactionGetReceipt = response.getTransactionGetReceipt();
            final ResponseCodeEnum precheckCode = transactionGetReceipt.getHeader()
                    .getNodeTransactionPrecheckCode();
            if (precheckCode != ResponseCodeEnum.OK) {
                throw new IllegalStateException("Transaction failed with precheck code: " + precheckCode);
            }
            final TransactionReceipt protoReceipt = transactionGetReceipt.getReceipt();
            return receiptFactory.apply(transactionId, protoReceipt);
        });
    }

    @Override
    public <R extends Record> CompletableFuture<R> queryTransactionRecord(TransactionId transactionId,
            BiFunction<TransactionId, TransactionRecord, R> recordFactory) {
        return null;
    }

    @Override
    public CompletableFuture<Receipt> queryTransactionReceipt(final @NonNull TransactionId transactionId) {
        return queryTransactionReceipt(transactionId, (id, protoReceipt) -> new DefaultReceipt(id));
    }
    
    @Override
    public @NonNull GrpcClient getGrpcClient() {
        return new GrpcClientImpl(networkSettings.getConsensusNodes().iterator().next(), executor);
    }

    @NonNull
    public Account getOperatorAccount() {
        return operatorAccount;
    }

    @NonNull
    public NetworkSettings getNetworkSettings() {
        return networkSettings;
    }
}
