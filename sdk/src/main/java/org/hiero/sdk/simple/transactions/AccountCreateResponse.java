package org.hiero.sdk.simple.transactions;

import org.hiero.sdk.simple.internal.AbstractResponse;
import org.hiero.sdk.simple.network.TransactionId;
import org.jspecify.annotations.NonNull;

public final class AccountCreateResponse extends AbstractResponse {

    public AccountCreateResponse(@NonNull final TransactionId transactionId) {
        super(transactionId);
    }
}
