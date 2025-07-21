package com.cowrite.project.exception;

public class AuthorizationException extends RuntimeException {
    private final Integer code;

    public AuthorizationException(String message) {
        super(message);
        this.code = 500;
    }

    public AuthorizationException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}