package org.hiero.sdk.simple.internal;

import org.hiero.sdk.simple.Receipt;
import org.hiero.sdk.simple.network.TransactionId;

public record DefaultReceipt(TransactionId transactionId) implements Receipt {

}
