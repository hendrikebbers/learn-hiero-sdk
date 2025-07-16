package org.hiero.sdk.simple.internal;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.TransactionId;
import com.hedera.hashgraph.sdk.proto.TransactionBody;
import io.grpc.MethodDescriptor;
import java.time.Duration;
import java.util.Objects;
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

    private String memo = "";

    @NonNull
    protected abstract T self();

    @NonNull
    protected abstract Class<R> getResponseType();

    @NonNull
    protected abstract MethodDescriptor<com.hedera.hashgraph.sdk.proto.Transaction, com.hedera.hashgraph.sdk.proto.TransactionResponse> getMethodDescriptor();

    @Override
    @NonNull
    public FrozenTransaction<R> freezeTransaction(@NonNull HieroClient client) {
        final AccountId nodeAccount = client.getNetworkSettings().getConsensusNodes().iterator().next().getAccountId();
        final TransactionBody transactionBody = buildTransactionBody(client.generateTransactionId(), nodeAccount);
        final ResponseFactory<R> responseFactory = ResponseFactory.forResponseType(getResponseType());
        final MethodDescriptor<com.hedera.hashgraph.sdk.proto.Transaction, com.hedera.hashgraph.sdk.proto.TransactionResponse> methodDescriptor = getMethodDescriptor();
        return new DefaultFrozenTransaction(methodDescriptor, transactionBody, responseFactory, client);
    }

    @NonNull
    private TransactionBody buildTransactionBody(@NonNull final TransactionId transactionId,
            @NonNull final AccountId nodeAccount) {
        Objects.requireNonNull(transactionId, "transactionId must not be null");
        Objects.requireNonNull(nodeAccount, "nodeAccount must not be null");
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
