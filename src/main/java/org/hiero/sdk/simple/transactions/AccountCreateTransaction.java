package org.hiero.sdk.simple.transactions;

import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.proto.CryptoCreateTransactionBody;
import com.hedera.hashgraph.sdk.proto.TransactionBody.Builder;
import com.hedera.hashgraph.sdk.proto.TransactionResponse;
import org.hiero.sdk.simple.internal.AbstractTransaction;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class AccountCreateTransaction extends AbstractTransaction<AccountCreateTransaction, AccountCreateResponse> {

    private String accountMemo;

    private Hbar initialBalance;

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
    protected void updateFrozenBodyBuilderWithSpecifics(Builder builder) {
        final CryptoCreateTransactionBody.Builder cryptoCreateBuilder = CryptoCreateTransactionBody.newBuilder()
                .setInitialBalance(initialBalance != null ? initialBalance.toTinybars() : 0)
                .setMemo(accountMemo);
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
        requireNotFrozen();
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
        requireNotFrozen();
        this.initialBalance = initialBalance;
    }

    @NonNull
    public AccountCreateTransaction withInitialBalance(final @Nullable Hbar initialBalance) {
        setInitialBalance(initialBalance);
        return self();
    }
}
