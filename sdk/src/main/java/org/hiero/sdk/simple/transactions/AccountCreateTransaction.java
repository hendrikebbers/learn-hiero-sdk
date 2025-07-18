package org.hiero.sdk.simple.transactions;

import com.google.protobuf.InvalidProtocolBufferException;
import com.hedera.hashgraph.sdk.proto.CryptoCreateTransactionBody;
import com.hedera.hashgraph.sdk.proto.Transaction;
import com.hedera.hashgraph.sdk.proto.TransactionBody;
import com.hedera.hashgraph.sdk.proto.TransactionBody.Builder;
import com.hedera.hashgraph.sdk.proto.TransactionResponse;
import io.grpc.MethodDescriptor;
import java.time.Duration;
import java.util.Objects;
import org.hiero.sdk.simple.internal.AbstractTransaction;
import org.hiero.sdk.simple.internal.grpc.CryptoServiceGrpc;
import org.hiero.sdk.simple.internal.util.ProtobufUtil;
import org.hiero.sdk.simple.network.Hbar;
import org.hiero.sdk.simple.network.HbarUnit;
import org.hiero.sdk.simple.network.TransactionId;
import org.hiero.sdk.simple.network.keys.Key;
import org.hiero.sdk.simple.transactions.spi.ResponseFactory;
import org.hiero.sdk.simple.transactions.spi.TransactionFactory;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public final class AccountCreateTransaction extends
        AbstractTransaction<AccountCreateTransaction, AccountCreateResponse> {

    private String accountMemo = "";

    private Hbar initialBalance = Hbar.ZERO;

    private Key key;

    @NonNull
    @Override
    protected AccountCreateTransaction self() {
        return this;
    }

    @Override
    protected @NonNull TransactionFactory<AccountCreateTransaction, AccountCreateResponse> getTransactionFactory() {
        return transactionBody -> {
            final CryptoCreateTransactionBody cryptoCreateBody = transactionBody.getCryptoCreateAccount();
            final AccountCreateTransaction transaction = new AccountCreateTransaction();
            transaction.setFee(Hbar.of(transactionBody.getTransactionFee(), HbarUnit.TINYBAR));
            transaction.setValidDuration(ProtobufUtil.fromProtobuf(transactionBody.getTransactionValidDuration()));
            transaction.setMemo(transactionBody.getMemo());
            transaction.setInitialBalance(Hbar.of(cryptoCreateBody.getInitialBalance(), HbarUnit.TINYBAR));
            transaction.setAccountMemo(cryptoCreateBody.getMemo());
            // TODO: cryptoCreateBody.getKey() conversion currently not supported
            return transaction;
        };
    }

    @Override
    protected @NonNull ResponseFactory<AccountCreateResponse> getResponseFactory() {
        return (client, protoTransaction, protoResponse) -> {
            try {
                final TransactionBody body = TransactionBody.parseFrom(protoTransaction.getBodyBytes());
                final TransactionId transactionId = ProtobufUtil.fromProtobuf(body.getTransactionID());
                return new AccountCreateResponse(client, transactionId);
            } catch (InvalidProtocolBufferException e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Override
    protected MethodDescriptor<Transaction, TransactionResponse> getMethodDescriptor() {
        return CryptoServiceGrpc.getCreateAccountMethod();
    }

    @Override
    protected void updateBodyBuilderWithSpecifics(@NonNull final Builder builder) {
        Objects.requireNonNull(builder, "builder must not be null");
        final CryptoCreateTransactionBody.Builder cryptoCreateBuilder = CryptoCreateTransactionBody.newBuilder();
        cryptoCreateBuilder.setInitialBalance(initialBalance != null ? initialBalance.tinybar() : 0);
        cryptoCreateBuilder.setMemo(accountMemo);
        cryptoCreateBuilder.setKey(ProtobufUtil.toKeyProtobuf(key));
        cryptoCreateBuilder.setAutoRenewPeriod(ProtobufUtil.toProtobuf(Duration.ofDays(90)));
        builder.setCryptoCreateAccount(cryptoCreateBuilder);
    }

    @Nullable
    public String getAccountMemo() {
        return accountMemo;
    }

    public void setAccountMemo(final @NonNull String accountMemo) {
        Objects.requireNonNull(accountMemo, "accountMemo must not be null");
        this.accountMemo = accountMemo;
    }

    @NonNull
    public AccountCreateTransaction withAccountMemo(final @Nullable String accountMemo) {
        setAccountMemo(accountMemo);
        return self();
    }

    @Nullable
    public Hbar getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(final @NonNull Hbar initialBalance) {
        Objects.requireNonNull(initialBalance, "initialBalance must not be null");
        this.initialBalance = initialBalance;
    }

    @NonNull
    public AccountCreateTransaction withInitialBalance(final @Nullable Hbar initialBalance) {
        setInitialBalance(initialBalance);
        return self();
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    @NonNull
    public AccountCreateTransaction withKey(@NonNull Key key) {
        setKey(key);
        return self();
    }
}
