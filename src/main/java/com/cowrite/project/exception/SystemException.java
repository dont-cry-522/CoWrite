package com.cowrite.project.exception;

/**
 * Encapsulate internal errors of the server, such as abnormal database and failure of third-party service call.
 *
 * @author heathcetide
 */
public class SystemException extends RuntimeException {

    /**
     * Error code, used in conjunction with ErrorCodeEnum.
     */
    private final Integer errorCode;

    /**
     * Error message
     */
    private final String errorDetail;

    public SystemException(Integer errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.errorDetail = null;
    }

    public SystemException(Integer errorCode, String message, String errorDetail) {
        super(message);
        this.errorCode = errorCode;
        this.errorDetail = errorDetail;
    }

    public SystemException(Integer errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorDetail = null;
    }

    public SystemException(Integer errorCode, String message, String errorDetail, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorDetail = errorDetail;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorDetail() {
        return errorDetail;
    }

    @Override
    public String toString() {
        return "SystemException{" +
                "errorCode=" + errorCode +
                ", message='" + getMessage() + '\'' +
                ", errorDetail='" + errorDetail + '\'' +
                ", cause='" + getCause() + '\'' +
                '}';
    }
}
