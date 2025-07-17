package org.hiero.sdk.simple.internal;

import com.hedera.hashgraph.sdk.proto.TransactionBody;
import io.grpc.MethodDescriptor;
import java.time.Duration;
import java.util.Objects;
import org.hiero.sdk.simple.FrozenTransaction;
import org.hiero.sdk.simple.HieroClient;
import org.hiero.sdk.simple.Response;
import org.hiero.sdk.simple.Transaction;
import org.hiero.sdk.simple.internal.util.ProtobufUtil;
import org.hiero.sdk.simple.network.AccountId;
import org.hiero.sdk.simple.network.Hbar;
import org.hiero.sdk.simple.network.TransactionId;
import org.hiero.sdk.simple.transactions.spi.ResponseFactory;
import org.hiero.sdk.simple.transactions.spi.TransactionFactory;
import org.jspecify.annotations.NonNull;

public abstract class AbstractTransaction<T extends Transaction, R extends Response> implements
        Transaction<T, R> {

    private Hbar fee = Hbar.ZERO;

    private Duration validDuration = Duration.ofSeconds(120);

    private String memo = "";

    @NonNull
    protected abstract T self();

    @NonNull
    protected abstract TransactionFactory<T, R> getTransactionFactory();

    @NonNull
    protected abstract ResponseFactory<R> getResponseFactory();

    @NonNull
    protected abstract MethodDescriptor<com.hedera.hashgraph.sdk.proto.Transaction, com.hedera.hashgraph.sdk.proto.TransactionResponse> getMethodDescriptor();

    @Override
    @NonNull
    public FrozenTransaction<T, R> freezeTransaction(@NonNull final HieroClient client) {
        final AccountId nodeAccount = client.getNetworkSettings().getConsensusNodes().iterator().next().getAccountId();
        final TransactionBody transactionBody = buildTransactionBody(client.generateTransactionId(), nodeAccount);
        final TransactionFactory<T, R> transactionFactory = getTransactionFactory();
        final ResponseFactory<R> responseFactory = getResponseFactory();
        final MethodDescriptor<com.hedera.hashgraph.sdk.proto.Transaction, com.hedera.hashgraph.sdk.proto.TransactionResponse> methodDescriptor = getMethodDescriptor();
        return new DefaultFrozenTransaction(methodDescriptor, transactionBody, transactionFactory, responseFactory,
                client);
    }

    @NonNull
    private TransactionBody buildTransactionBody(@NonNull final TransactionId transactionId,
            @NonNull final AccountId nodeAccount) {
        Objects.requireNonNull(transactionId, "transactionId must not be null");
        Objects.requireNonNull(nodeAccount, "nodeAccount must not be null");
        final TransactionBody.Builder builder = TransactionBody.newBuilder()
                .setTransactionID(ProtobufUtil.toProtobuf(transactionId))
                .setNodeAccountID(ProtobufUtil.toProtobuf(nodeAccount))
                .setTransactionFee(fee.tinybar())
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
