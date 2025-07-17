package org.hiero.sdk.simple.transactions;

import org.hiero.sdk.simple.Response;
import org.hiero.sdk.simple.network.TransactionId;

public record AccountCreateResponse(TransactionId transactionId) implements Response {
}
