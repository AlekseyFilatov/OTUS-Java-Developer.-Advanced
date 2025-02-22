package com.otus.grpc.restapi.exception;

public class GRPCRuntimeException extends RuntimeException {
    public GRPCRuntimeException() {
    }

    public GRPCRuntimeException(String id, Exception e) {
        super(String.format("GRPCServerRuntimeException runtime exception Id : %s", id) , e);
    }
}