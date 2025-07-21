package com.cowrite.project.common;

import com.cowrite.project.common.enums.ResponseCodeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 统一请求响应类
 *
 * @author heathcetide
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse<T> {
    private int code; // 修改为 int 类型
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(ResponseCodeEnum.SUCCESS.code()); // 假设 ResponseCodeEnum.SUCCESS.code() 返回的是 int
        response.setMessage(ResponseCodeEnum.SUCCESS.message());
        response.setData(data);
        return response;
    }

    public static <T> ApiResponse<T> error(int code, String message) { // 修改参数为 int
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    public static <T> ApiResponse<T> success() {
        return success(null);
    }

    public static <T> ApiResponse<T> error(String message) {
        return error(ResponseCodeEnum.SYSTEM_ERROR.code(), message); // 假设 SYSTEM_ERROR.code() 返回的是 int
    }

    public static <T> ApiResponse<T> badRequest(String message) {
        return error(ResponseCodeEnum.NOT_FOUND.code(), message); // 假设 NOT_FOUND.code() 返回的是 int
    }

    public int getCode() { // 修改返回类型为 int
        return code;
    }

    public void setCode(int code) { // 修改参数为 int
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    // 添加链式调用方法
    public ApiResponse<T> code(int code) { // 修改参数为 int
        this.code = code;
        return this;
    }

    public ApiResponse<T> message(String message) {
        this.message = message;
        return this;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
