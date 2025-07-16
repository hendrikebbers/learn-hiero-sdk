package org.hiero.sdk.simple.grpc;

import java.util.ServiceLoader;
import org.hiero.sdk.simple.TransactionResponse;

public interface ResponseFactory<R extends TransactionResponse> {

    Class<R> getSupportedResponseType();

    R createResponse(com.hedera.hashgraph.sdk.proto.TransactionResponse grpcResponse);

    static <TYPE extends TransactionResponse> ResponseFactory<TYPE> forResponseType(Class<TYPE> cls) {
        return ServiceLoader.load(ResponseFactory.class).stream()
                .map(p -> p.get())
                .filter(factory -> factory.getSupportedResponseType().equals(cls))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No ResponseFactory found for " + cls));
    }
}
