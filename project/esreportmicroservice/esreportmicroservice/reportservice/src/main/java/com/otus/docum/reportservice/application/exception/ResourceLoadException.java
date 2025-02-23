package com.otus.docum.reportservice.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource not found")
public class ResourceLoadException extends RuntimeException{
    public ResourceLoadException() {
    }

    public ResourceLoadException(String fileName, Exception e) {
        super(String.format("FileService runtime exception in : %s", fileName) , e);
    }
}
