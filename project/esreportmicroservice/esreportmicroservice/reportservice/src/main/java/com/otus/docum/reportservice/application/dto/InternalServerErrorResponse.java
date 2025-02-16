package com.otus.docum.reportservice.application.dto;

public record InternalServerErrorResponse(int status, String message, String timestamp) {
}
