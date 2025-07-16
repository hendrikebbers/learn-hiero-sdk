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
import org.hiero.sdk.simple.FrozenTransaction;
import org.hiero.sdk.simple.HieroClient;
import org.hiero.sdk.simple.Transaction;
import org.hiero.sdk.simple.TransactionResponse;
import org.hiero.sdk.simple.internal.grpc.GrpcClientImpl;
import org.hiero.sdk.simple.internal.util.KeyUtils;
import org.hiero.sdk.simple.internal.util.ProtobufUtil;
import org.hiero.sdk.simple.transactions.ResponseFactory;
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

    protected abstract Class<R> getResponseType();

    @Override
    public FrozenTransaction<R> freezeTransaction(@NonNull HieroClient client) {
        TransactionBody transactionBody = buildTransactionBody(client.generateTransactionId());
        ResponseFactory<R> responseFactory = ResponseFactory.forResponseType(getResponseType());
        return new DefaultFrozenTransaction(transactionBody, responseFactory, client);
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
