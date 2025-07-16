package org.hiero.sdk.simple.internal;

import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PublicKey;
import com.hedera.hashgraph.sdk.TransactionId;
import com.hedera.hashgraph.sdk.proto.SignatureMap;
import com.hedera.hashgraph.sdk.proto.TransactionBody;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;
import org.hiero.sdk.simple.HieroClient;
import org.hiero.sdk.simple.Transaction;
import org.hiero.sdk.simple.TransactionResponse;
import org.hiero.sdk.simple.internal.grpc.GrpcClient;
import org.hiero.sdk.simple.internal.util.KeyUtils;
import org.hiero.sdk.simple.internal.util.ProtobufUtil;
import org.jspecify.annotations.NonNull;

public abstract class AbstractTransaction<T extends Transaction, R extends TransactionResponse> implements
        Transaction<T, R> {

    private Hbar fee;

    private Duration validDuration;

    private String memo;

    private boolean frozen = false;

    private final Map<PublicKey, byte[]> transactionSignatures = new HashMap<>();

    private TransactionBody transactionBody;

    @NonNull
    protected abstract T self();

    @Override
    public T freeze(HieroClient client) {
        frozen = true;
        transactionBody = buildTransactionBody(client.generateTransactionId());
        return self();
    }

    private TransactionBody buildTransactionBody(@NonNull final TransactionId transactionId) {
        final TransactionBody.Builder frozenBodyBuilder = TransactionBody.newBuilder()
                .setTransactionID(ProtobufUtil.toProtobuf(transactionId))
                .setTransactionFee(fee.toTinybars())
                .setTransactionValidDuration(ProtobufUtil.toProtobuf(validDuration).toBuilder())
                .setMemo(memo);
        updateFrozenBodyBuilderWithSpecifics(frozenBodyBuilder);
        return frozenBodyBuilder.build();
    }

    protected abstract void updateFrozenBodyBuilderWithSpecifics(TransactionBody.Builder builder);

    protected void requireNotFrozen() {
        if (isFrozen()) {
            throw new IllegalStateException(
                    "transaction is immutable; it has at least one signature or has been explicitly frozen");
        }
    }

    private boolean isFrozen() {
        return frozen;
    }

    public T sign(@NonNull final PublicKey publicKey, @NonNull final UnaryOperator<byte[]> transactionSigner) {
        Objects.requireNonNull(publicKey, "publicKey must not be null");
        Objects.requireNonNull(transactionSigner, "transactionSigner must not be null");
        if (!isFrozen()) {
            throw new IllegalStateException("transaction must be frozen before signing");
        }
        if (transactionSignatures.containsKey(publicKey)) {
            throw new IllegalStateException("transaction is already signed with public key '" + publicKey + "'");
        }
        if (transactionBody == null) {
            throw new IllegalStateException("transaction body is not built; call freeze() before signing");
        }
        final byte[] transactionBytes = transactionBody.toByteArray();
        final byte[] signature = transactionSigner.apply(transactionBytes);
        transactionSignatures.put(publicKey, signature);
        return self();
    }

    @Override
    public @NonNull CompletableFuture<R> execute(@NonNull HieroClient client) {
        Objects.requireNonNull(client, "client must not be null");
        if (!isFrozen()) {
            throw new IllegalStateException("transaction must be frozen before execution");
        }
        if (transactionBody == null) {
            throw new IllegalStateException("transaction body is not built; call freeze() before executing");
        }
        if (transactionSignatures.isEmpty()) {
            throw new IllegalStateException("transaction must be signed before execution");
        }
        if (!transactionSignatures.containsKey(client.getOperatorAccount().publicKey())) {
            throw new IllegalStateException("transaction must be signed with operator's public key before execution");
        }
        com.hedera.hashgraph.sdk.proto.Transaction protobufTransaction = createProtobufTransaction();
        GrpcClient grpcClient = null;
        return grpcClient.sendTransaction(protobufTransaction).handle((response, throwable) -> {
            if (throwable != null) {
                throw new RuntimeException("Transaction execution failed", throwable);
            }
            return createResponseFromProtobuf(response);
        });
    }

    protected abstract R createResponseFromProtobuf(
            com.hedera.hashgraph.sdk.proto.TransactionResponse response);

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

    public Hbar getFee() {
        return fee;
    }

    public void setFee(Hbar fee) {
        this.fee = fee;
    }

    public T withFee(Hbar fee) {
        setFee(fee);
        return self();
    }

    public Duration getValidDuration() {
        return validDuration;
    }

    public void setValidDuration(Duration validDuration) {
        this.validDuration = validDuration;
    }

    public T withValidDuration(Duration validDuration) {
        setValidDuration(validDuration);
        return self();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public T withMemo(String memo) {
        setMemo(memo);
        return self();
    }
}
