package org.hiero.sdk.simple.internal.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

public final class CryptoServiceGrpc {

    private static volatile io.grpc.MethodDescriptor<com.hedera.hashgraph.sdk.proto.Transaction,
            com.hedera.hashgraph.sdk.proto.TransactionResponse> getCreateAccountMethod;

    private static volatile io.grpc.MethodDescriptor<com.hedera.hashgraph.sdk.proto.Query,
            com.hedera.hashgraph.sdk.proto.Response> getGetTransactionReceiptsMethod;

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

    public static io.grpc.MethodDescriptor<com.hedera.hashgraph.sdk.proto.Query,
            com.hedera.hashgraph.sdk.proto.Response> getGetTransactionReceiptsMethod() {
        io.grpc.MethodDescriptor<com.hedera.hashgraph.sdk.proto.Query, com.hedera.hashgraph.sdk.proto.Response> getGetTransactionReceiptsMethod;
        if ((getGetTransactionReceiptsMethod = CryptoServiceGrpc.getGetTransactionReceiptsMethod) == null) {
            synchronized (CryptoServiceGrpc.class) {
                if ((getGetTransactionReceiptsMethod = CryptoServiceGrpc.getGetTransactionReceiptsMethod) == null) {
                    CryptoServiceGrpc.getGetTransactionReceiptsMethod = getGetTransactionReceiptsMethod =
                            io.grpc.MethodDescriptor.<com.hedera.hashgraph.sdk.proto.Query, com.hedera.hashgraph.sdk.proto.Response>newBuilder()
                                    .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                                    .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getTransactionReceipts"))
                                    .setSampledToLocalTracing(true)
                                    .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                                            com.hedera.hashgraph.sdk.proto.Query.getDefaultInstance()))
                                    .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                                            com.hedera.hashgraph.sdk.proto.Response.getDefaultInstance()))
                                    .build();
                }
            }
        }
        return getGetTransactionReceiptsMethod;
    }
}
