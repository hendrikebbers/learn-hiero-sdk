package org.hiero.sdk.simple.transactions;


import org.hiero.sdk.simple.TransactionResponse;

public interface ResponseFactory<R extends TransactionResponse> {

    R createResponse(com.hedera.hashgraph.sdk.proto.TransactionResponse grpcResponse);

    static ResponseFactory forResponseType(Class<? extends TransactionResponse> cls) {
        return null; // This method should return an appropriate ResponseFactory based on the class type
    }
}
