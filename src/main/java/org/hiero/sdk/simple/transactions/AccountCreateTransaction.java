package org.hiero.sdk.simple.transactions;

import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.proto.CryptoCreateTransactionBody;
import com.hedera.hashgraph.sdk.proto.CryptoServiceGrpc;
import com.hedera.hashgraph.sdk.proto.Transaction;
import com.hedera.hashgraph.sdk.proto.TransactionBody.Builder;
import com.hedera.hashgraph.sdk.proto.TransactionResponse;
import io.grpc.MethodDescriptor;
import java.time.Duration;
import org.hiero.sdk.simple.internal.AbstractTransaction;
import org.hiero.sdk.simple.internal.util.KeyUtils;
import org.hiero.sdk.simple.internal.util.ProtobufUtil;
import org.hiero.sdk.simple.network.keys.Key;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class AccountCreateTransaction extends AbstractTransaction<AccountCreateTransaction, AccountCreateResponse> {

    private String accountMemo;

    private Hbar initialBalance;

    private Key key;

    @NonNull
    @Override
    protected AccountCreateTransaction self() {
        return this;
    }

    @Override
    protected Class<AccountCreateResponse> getResponseType() {
        return AccountCreateResponse.class;
    }

    @Override
    protected MethodDescriptor<Transaction, TransactionResponse> getMethodDescriptor() {
        return CryptoServiceGrpc.getCreateAccountMethod();
    }

    @Override
    protected void updateBodyBuilderWithSpecifics(Builder builder) {
        final CryptoCreateTransactionBody.Builder cryptoCreateBuilder = CryptoCreateTransactionBody.newBuilder();
        cryptoCreateBuilder.setInitialBalance(initialBalance != null ? initialBalance.toTinybars() : 0);
        cryptoCreateBuilder.setMemo(accountMemo);
        cryptoCreateBuilder.setKey(KeyUtils.toKeyProtobuf(key));
        cryptoCreateBuilder.setAutoRenewPeriod(ProtobufUtil.toProtobuf(Duration.ofDays(90)));
        builder.setCryptoCreateAccount(cryptoCreateBuilder);
    }

    protected AccountCreateResponse createResponseFromProtobuf(TransactionResponse response) {
        //TODO: Implement response parsing to extract account ID
        return new AccountCreateResponse(null);
    }

    @Nullable
    public String getAccountMemo() {
        return accountMemo;
    }

    public void setAccountMemo(final @Nullable String accountMemo) {
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

    public void setInitialBalance(final @Nullable Hbar initialBalance) {
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
