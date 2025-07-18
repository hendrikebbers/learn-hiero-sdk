package org.hiero.sdk.simple.internal;

import com.hedera.hashgraph.sdk.proto.SignatureMap;
import com.hedera.hashgraph.sdk.proto.Transaction;
import com.hedera.hashgraph.sdk.proto.TransactionBody;
import io.grpc.MethodDescriptor;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.UnaryOperator;
import org.hiero.sdk.simple.FrozenTransaction;
import org.hiero.sdk.simple.HieroClient;
import org.hiero.sdk.simple.Response;
import org.hiero.sdk.simple.grpc.GrpcClient;
import org.hiero.sdk.simple.internal.util.ProtobufUtil;
import org.hiero.sdk.simple.network.keys.PublicKey;
import org.hiero.sdk.simple.transactions.spi.ResponseFactory;
import org.hiero.sdk.simple.transactions.spi.TransactionFactory;
import org.jspecify.annotations.NonNull;

public class DefaultFrozenTransaction<R extends Response, T extends org.hiero.sdk.simple.Transaction> implements
        FrozenTransaction<T, R> {

    private final MethodDescriptor<Transaction, com.hedera.hashgraph.sdk.proto.TransactionResponse> methodDescriptor;

    private final Map<PublicKey, byte[]> transactionSignatures = new HashMap<>();

    private final TransactionBody transactionBody;

    private final HieroClient client;

    private final TransactionFactory<T, R> transactionFactory;

    private final ResponseFactory<R> responseFactory;

    public DefaultFrozenTransaction(
            @NonNull final MethodDescriptor<Transaction, com.hedera.hashgraph.sdk.proto.TransactionResponse> methodDescriptor,
            @NonNull final TransactionBody transactionBody,
            @NonNull final TransactionFactory<T, R> transactionFactory,
            @NonNull final ResponseFactory<R> responseFactory,
            @NonNull final HieroClient client) {
        this.methodDescriptor = Objects.requireNonNull(methodDescriptor, "methodDescriptor must not be null");
        this.transactionBody = Objects.requireNonNull(transactionBody, "transactionBody must not be null");
        this.client = Objects.requireNonNull(client, "client must not be null");
        this.transactionFactory = Objects.requireNonNull(transactionFactory, "transactionFactory must not be null");
        this.responseFactory = Objects.requireNonNull(responseFactory, "responseFactory must not be null");
        if (client.signAutomaticallyWithOperator()) {
            sign(client.getOperatorAccount().privateKey());
        }
    }

    @Override
    public @NonNull FrozenTransaction sign(@NonNull final PublicKey publicKey,
            @NonNull final UnaryOperator<byte[]> transactionSigner) {
        Objects.requireNonNull(publicKey, "publicKey must not be null");
        Objects.requireNonNull(transactionSigner, "transactionSigner must not be null");

        if (transactionSignatures.containsKey(publicKey)) {
            throw new IllegalStateException("transaction is already signed with public key '" + publicKey + "'");
        }
        final byte[] transactionBytes = transactionBody.toByteArray();
        final byte[] signature = transactionSigner.apply(transactionBytes);
        transactionSignatures.put(publicKey, signature);
        return this;
    }

    @Override
    public CompletableFuture<R> execute() {
        Objects.requireNonNull(client, "client must not be null");
        final Transaction protobufTransaction = createProtobufTransaction();
        final GrpcClient grpcClient = client.getGrpcClient();
        return grpcClient.call(methodDescriptor, protobufTransaction).handle((response, throwable) -> {
            if (throwable != null) {
                throw new RuntimeException("Transaction execution failed", throwable);
            }
            return responseFactory.createResponse(protobufTransaction, response);
        });
    }

    @Override
    public R executeAndWait() throws ExecutionException, InterruptedException {
        return execute().get();
    }

    private Transaction createProtobufTransaction() {
        if (transactionBody == null) {
            throw new IllegalStateException(
                    "transaction body is not built; call freeze() before creating protobuf transaction");
        }
        final SignatureMap.Builder signatureBuilder = SignatureMap.newBuilder();
        transactionSignatures.entrySet().forEach(entry -> {
            signatureBuilder.addSigPair(ProtobufUtil.toSignaturePairProtobuf(entry.getKey(), entry.getValue()));
        });
        return Transaction.newBuilder()
                .setBodyBytes(transactionBody.toByteString())
                .setSigMap(signatureBuilder.build())
                .build();
    }

    @Override
    public T unpack() {
        return transactionFactory.unpack(transactionBody);
    }
}
