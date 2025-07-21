package com.cowrite.project.exception;

/**
 * User-defined parameter verification exception is used to encapsulate the fields and error messages that failed verification.
 *
 * @author heathcetide
 */
public class ValidationException extends RuntimeException {

    /**
     * Error code, used in conjunction with ErrorCodeEnum.
     */
    private final String errorCode;

    /**
     * Name of the field that failed validation.
     */
    private final String field;

    public ValidationException(String errorCode, String message, String field) {
        super(message);
        this.errorCode = errorCode;
        this.field = field;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getField() {
        return field;
    }

    @Override
    public String toString() {
        return "ValidationException{" +
                "errorCode='" + errorCode + '\'' +
                ", field='" + field + '\'' +
                ", message='" + getMessage() + '\'' +
                '}';
    }
}
