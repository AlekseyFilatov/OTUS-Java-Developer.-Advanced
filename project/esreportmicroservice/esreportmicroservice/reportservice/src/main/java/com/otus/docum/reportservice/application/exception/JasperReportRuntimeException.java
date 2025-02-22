package com.otus.docum.reportservice.application.exception;

public class JasperReportRuntimeException extends RuntimeException {
    public JasperReportRuntimeException() {
    }

    public JasperReportRuntimeException(String reportName, Exception e) {
        super(String.format("JasperReport runtime exception in report : %s", reportName) , e);
    }
}