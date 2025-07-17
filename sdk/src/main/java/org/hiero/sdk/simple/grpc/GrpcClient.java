package org.hiero.sdk.simple.grpc;

import com.hedera.hashgraph.sdk.proto.Transaction;
import com.hedera.hashgraph.sdk.proto.TransactionResponse;
import io.grpc.MethodDescriptor;
import java.util.concurrent.CompletableFuture;

public interface GrpcClient {

    CompletableFuture<TransactionResponse> sendTransaction(Transaction request,
            MethodDescriptor<Transaction, TransactionResponse> methodDescriptor);
}
