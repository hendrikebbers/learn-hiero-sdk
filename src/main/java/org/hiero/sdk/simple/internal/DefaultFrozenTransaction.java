package org.hiero.sdk.simple.internal;

import com.hedera.hashgraph.sdk.PublicKey;
import com.hedera.hashgraph.sdk.proto.SignatureMap;
import com.hedera.hashgraph.sdk.proto.TransactionBody;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.UnaryOperator;
import org.hiero.sdk.simple.FrozenTransaction;
import org.hiero.sdk.simple.GrpcClient;
import org.hiero.sdk.simple.HieroClient;
import org.hiero.sdk.simple.TransactionResponse;
import org.hiero.sdk.simple.internal.util.KeyUtils;
import org.hiero.sdk.simple.transactions.ResponseFactory;
import org.jspecify.annotations.NonNull;

public class DefaultFrozenTransaction<R extends TransactionResponse> implements FrozenTransaction<R> {

    private final Map<PublicKey, byte[]> transactionSignatures = new HashMap<>();

    private final TransactionBody transactionBody;

    private final HieroClient client;

    private final ResponseFactory<R> responseFactory;

    public DefaultFrozenTransaction(TransactionBody transactionBody, ResponseFactory<R> responseFactory, HieroClient client) {
        this.transactionBody = transactionBody;
        this.client = client;
        this.responseFactory = responseFactory;
    }

    @Override
    public @NonNull FrozenTransaction sign(@NonNull PublicKey publicKey,
            @NonNull UnaryOperator<byte[]> transactionSigner) {
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
        final com.hedera.hashgraph.sdk.proto.Transaction protobufTransaction = createProtobufTransaction();
        final GrpcClient grpcClient = client.getGrpcClient();
        return grpcClient.sendTransaction(protobufTransaction).handle((response, throwable) -> {
            if (throwable != null) {
                throw new RuntimeException("Transaction execution failed", throwable);
            }
            return responseFactory.createResponse(response);
        });
    }

    @Override
    public R executeAndWait() throws ExecutionException, InterruptedException {
        return execute().get();
    }

    private com.hedera.hashgraph.sdk.proto.Transaction createProtobufTransaction() {
        if (transactionBody == null) {
            throw new IllegalStateException(
                    "transaction body is not built; call freeze() before creating protobuf transaction");
        }
        final SignatureMap.Builder signatureBuilder = SignatureMap.newBuilder();
        transactionSignatures.entrySet().forEach(entry -> {
            signatureBuilder.addSigPair(KeyUtils.toSignaturePairProtobuf(entry.getKey(), entry.getValue()));
        });
        return com.hedera.hashgraph.sdk.proto.Transaction.newBuilder()
                .setBodyBytes(transactionBody.toByteString())
                .setSigMap(signatureBuilder.build())
                .build();
    }

}
