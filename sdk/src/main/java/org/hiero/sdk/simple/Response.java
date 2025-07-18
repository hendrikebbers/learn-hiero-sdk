package org.hiero.sdk.simple;

import java.util.concurrent.CompletableFuture;
import org.hiero.sdk.simple.network.TransactionId;

public interface Response {

    TransactionId transactionId();

    CompletableFuture<Receipt> getReceipt(HieroClient client);
}
