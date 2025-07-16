package org.hiero.sdk.simple.internal;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PublicKey;
import com.hedera.hashgraph.sdk.TransactionId;
import com.hedera.hashgraph.sdk.proto.TransactionBody;
import io.grpc.MethodDescriptor;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.hiero.sdk.simple.FrozenTransaction;
import org.hiero.sdk.simple.HieroClient;
import org.hiero.sdk.simple.Transaction;
import org.hiero.sdk.simple.TransactionResponse;
import org.hiero.sdk.simple.grpc.ResponseFactory;
import org.hiero.sdk.simple.internal.util.ProtobufUtil;
import org.jspecify.annotations.NonNull;

public abstract class AbstractTransaction<T extends Transaction, R extends TransactionResponse> implements
        Transaction<T, R> {

    private Hbar fee = Hbar.ZERO;

    private Duration validDuration = Duration.ofSeconds(120);

    private String memo;

    private boolean frozen = false;

    private final Map<PublicKey, byte[]> transactionSignatures = new HashMap<>();

    private TransactionBody transactionBody;

    @NonNull
    protected abstract T self();

    protected abstract Class<R> getResponseType();

    protected abstract MethodDescriptor<com.hedera.hashgraph.sdk.proto.Transaction, com.hedera.hashgraph.sdk.proto.TransactionResponse> getMethodDescriptor();

    @Override
    public FrozenTransaction<R> freezeTransaction(@NonNull HieroClient client) {
        final AccountId nodeAccount = client.getNetworkSettings().getConsensusNodes().iterator().next().getAccountId();
        TransactionBody transactionBody = buildTransactionBody(client.generateTransactionId(), nodeAccount);
        ResponseFactory<R> responseFactory = ResponseFactory.forResponseType(getResponseType());
        return new DefaultFrozenTransaction(getMethodDescriptor(), transactionBody, responseFactory, client);
    }

    private TransactionBody buildTransactionBody(@NonNull final TransactionId transactionId,
            final AccountId nodeAccount) {
        final TransactionBody.Builder builder = TransactionBody.newBuilder()
                .setTransactionID(ProtobufUtil.toProtobuf(transactionId))
                .setNodeAccountID(ProtobufUtil.toProtobuf(nodeAccount))
                .setTransactionFee(fee.toTinybars())
                .setTransactionValidDuration(ProtobufUtil.toProtobuf(validDuration).toBuilder())
                .setMemo(memo);
        updateBodyBuilderWithSpecifics(builder);
        return builder.build();
    }

    protected abstract void updateBodyBuilderWithSpecifics(TransactionBody.Builder builder);

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
