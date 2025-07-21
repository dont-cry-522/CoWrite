package com.cowrite.project.exception;

/**
 * 自定义参数校验异常，用于封装校验失败的字段和错误信息。
 *
 * @author heathcetide
 */
public class ValidationException extends RuntimeException {

    /**
     * 错误码，配合 ErrorCodeEnum 使用。
     */
    private final String errorCode;

    /**
     * 校验失败的字段名称。
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
