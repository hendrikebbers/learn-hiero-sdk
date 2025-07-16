package org.hiero.sdk.simple;

import com.hedera.hashgraph.sdk.proto.TransactionResponse;
import java.util.concurrent.CompletableFuture;

public interface GrpcClient {

    <Request extends com.hedera.hashgraph.sdk.proto.Transaction> CompletableFuture<TransactionResponse> sendTransaction(
            Request request);
}
