package org.hiero.sdk.simple;

import org.hiero.sdk.simple.network.TransactionId;

public interface Record<R extends Receipt> {

    default TransactionId transactionId() {
        return receipt().transactionId();
    }

    R receipt();
}
