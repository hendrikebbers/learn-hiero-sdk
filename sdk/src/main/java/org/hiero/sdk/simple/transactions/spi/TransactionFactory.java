package org.hiero.sdk.simple.transactions.spi;

import com.hedera.hashgraph.sdk.proto.TransactionBody;
import org.hiero.sdk.simple.Response;
import org.hiero.sdk.simple.Transaction;

public interface TransactionFactory<T extends Transaction, R extends Response> {

    T unpack(TransactionBody transactionBody);
}
