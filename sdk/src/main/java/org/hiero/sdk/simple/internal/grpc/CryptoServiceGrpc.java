package org.hiero.sdk.simple.internal.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

public class CryptoServiceGrpc {

    private static volatile io.grpc.MethodDescriptor<com.hedera.hashgraph.sdk.proto.Transaction,
            com.hedera.hashgraph.sdk.proto.TransactionResponse> getCreateAccountMethod;

    public static final java.lang.String SERVICE_NAME = "proto.CryptoService";
    
    public static io.grpc.MethodDescriptor<com.hedera.hashgraph.sdk.proto.Transaction,
            com.hedera.hashgraph.sdk.proto.TransactionResponse> getCreateAccountMethod() {
        io.grpc.MethodDescriptor<com.hedera.hashgraph.sdk.proto.Transaction, com.hedera.hashgraph.sdk.proto.TransactionResponse> getCreateAccountMethod;
        if ((getCreateAccountMethod = CryptoServiceGrpc.getCreateAccountMethod) == null) {
            synchronized (CryptoServiceGrpc.class) {
                if ((getCreateAccountMethod = CryptoServiceGrpc.getCreateAccountMethod) == null) {
                    CryptoServiceGrpc.getCreateAccountMethod = getCreateAccountMethod =
                            io.grpc.MethodDescriptor.<com.hedera.hashgraph.sdk.proto.Transaction, com.hedera.hashgraph.sdk.proto.TransactionResponse>newBuilder()
                                    .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                                    .setFullMethodName(generateFullMethodName(SERVICE_NAME, "createAccount"))
                                    .setSampledToLocalTracing(true)
                                    .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                                            com.hedera.hashgraph.sdk.proto.Transaction.getDefaultInstance()))
                                    .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                                            com.hedera.hashgraph.sdk.proto.TransactionResponse.getDefaultInstance()))
                                    .build();
                }
            }
        }
        return getCreateAccountMethod;
    }
}
