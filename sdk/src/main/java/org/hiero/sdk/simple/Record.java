package org.hiero.sdk.simple;

import org.hiero.sdk.simple.network.TransactionId;

public interface Record<R extends Receipt> {

    TransactionId transactionId();

    R receipt();
}
