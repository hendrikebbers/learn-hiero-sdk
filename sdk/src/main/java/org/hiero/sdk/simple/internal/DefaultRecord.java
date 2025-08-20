package org.hiero.sdk.simple.internal;

import org.hiero.sdk.simple.Record;
import org.hiero.sdk.simple.network.TransactionId;

public record DefaultRecord(TransactionId transactionId, DefaultReceipt receipt) implements
        Record<DefaultReceipt> {

}
