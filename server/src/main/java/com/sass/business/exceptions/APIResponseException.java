package com.sass.business.exceptions;

public class APIResponseException extends RuntimeException {
    private final int status;

    public APIResponseException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getHttpStatus() {
        return status;
    }
}
