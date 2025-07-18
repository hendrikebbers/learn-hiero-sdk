package org.hiero.sdk.simple.transactions.spi;

import com.hedera.hashgraph.sdk.proto.Transaction;
import com.hedera.hashgraph.sdk.proto.TransactionResponse;
import org.hiero.sdk.simple.HieroClient;
import org.hiero.sdk.simple.Response;

public interface ResponseFactory<R extends Response> {

    R createResponse(HieroClient client, Transaction protoTransaction, TransactionResponse protoResponse);

}
