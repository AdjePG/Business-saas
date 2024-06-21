package com.sass.business.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponse<T> {
    // region ATTRIBUTES

    private int status;
    private String message;
    private T result;

    // endregion

    // region CONSTRUCTOR

    public APIResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public APIResponse(int status, String message, T result) {
        this.status = status;
        this.message = message;
        this.result = result;
    }

    // endregion

    // region GETTERS AND SETTERS

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    // endregion
}
