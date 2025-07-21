package com.cowrite.project.exception;

import com.cowrite.project.common.ApiResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static com.cowrite.project.common.enums.ResponseCodeEnum.UNAUTHORIZED;


/**
 * 全局异常处理器，统一拦截并处理项目中抛出的异常，返回结构化响应。
 *
 * @author heathcetide
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常（如余额不足、状态冲突等）
     */
    @ExceptionHandler(BusinessException.class)
    public ApiResponse<?> handleBusinessException(BusinessException ex) {
        return ApiResponse.error(ex.getErrorCode(), ex.getMessage());
    }

    /**
     * 处理系统级异常（如数据库连接失败、第三方服务不可用等）
     */
    @ExceptionHandler(SystemException.class)
    public ApiResponse<?> handleSystemException(SystemException ex) {
        return ApiResponse.error(ex.getErrorCode(), ex.getMessage());
    }

    /**
     * 处理认证或授权失败异常（如未登录、没有权限访问等）
     */
    @ExceptionHandler(AuthException.class)
    public ApiResponse<?> handleAuthException(AuthException ex) {
        return ApiResponse.error(ex.getErrorCode(), ex.getMessage());
    }

    /**
     * 处理资源未找到异常（如访问了不存在的用户、商品等）
     */
    @ExceptionHandler(NotFoundException.class)
    public ApiResponse<?> handleNotFoundException(NotFoundException ex) {
        return ApiResponse.error(ex.getErrorCode(), ex.getMessage());
    }

    /**
     * 处理资源未找到异常（如访问了不存在的用户、商品等）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return ApiResponse.badRequest(errors.toString());
    }

    /**
     * 处理权限认证异常
     */
    @ExceptionHandler(AuthorizationException.class)
    public ApiResponse<?> handleAuthorizationException(AuthorizationException e) {
        return ApiResponse.error(UNAUTHORIZED.code(),  e.getMessage());
    }

    /**
     * 处理运行异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<?> handleRuntimeException(RuntimeException e) {
        return ApiResponse.error(UNAUTHORIZED.code(),  e.getMessage());
    }

    /**
     * 捕获所有未明确处理的其他异常，避免 500 错误直接暴露
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleOther(Exception ex) {
        return ApiResponse.error(ex.getMessage());
    }

}
