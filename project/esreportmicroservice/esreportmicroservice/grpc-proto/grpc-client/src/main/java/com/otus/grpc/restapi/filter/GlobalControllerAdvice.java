package com.otus.grpc.restapi.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.otus.grpc.restapi.exception.GRPCRuntimeException;

import io.grpc.reflection.v1alpha.ErrorResponse;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

//https://www.kapresoft.com/java/2023/05/15/spring-error-handling-best-practices.html
@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
@Order(2)
public class GlobalControllerAdvice  {

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(GRPCRuntimeException.class)
    public ResponseEntity<String> grpcRuntimeException(GRPCRuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getCause().getMessage());
    }
}
