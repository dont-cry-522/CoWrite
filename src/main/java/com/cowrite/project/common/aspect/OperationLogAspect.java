package com.cowrite.project.common.aspect;


import com.cowrite.project.common.anno.Loggable;
import com.cowrite.project.common.context.RequestContext;
import com.cowrite.project.model.entity.OperationLog;
import com.cowrite.project.model.entity.User;
import com.cowrite.project.service.OperationLogService;
import com.cowrite.project.service.UserService;
import com.cowrite.project.utils.JsonUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.cowrite.project.common.constants.UserConstants.CURRENT_USERNAME;
import static com.cowrite.project.common.constants.UserConstants.DEFAULT_USERNAME;


/**
 * operation log section
 * Used to intercept the method annotated by @Loggable, and record the operation log after the method is successfully executed or an exception is thrown.
 * The recorded information includes: operation type, description, operator, success, requested parameters and returned results.
 *
 * @author heathcetide
 */
@Aspect
@Component
public class OperationLogAspect {

    private final OperationLogService logService;

    private final UserService userService;

    public OperationLogAspect(OperationLogService logService, UserService userService) {
        this.logService = logService;
        this.userService = userService;
    }

    /**
     * 成功返回后执行日志记录
     */
    @AfterReturning(pointcut = "@annotation(loggable)", returning = "result")
    public void logSuccess(JoinPoint jp, Loggable loggable, Object result) {
        saveLog(jp, loggable, result, null);
    }

    /**
     * 方法抛出异常后执行日志记录
     */
    @AfterThrowing(pointcut = "@annotation(loggable)", throwing = "ex")
    public void logError(JoinPoint jp, Loggable loggable, Exception ex) {
        saveLog(jp, loggable, null, ex);
    }

    /**
     * 核心日志保存方法
     */
    private void saveLog(JoinPoint jp, Loggable loggable, Object result, Exception ex) {
        OperationLog log = new OperationLog();
        log.setType(loggable.type());
        log.setDescription(loggable.value());
        User currentUser = userService.getCurrentUser();
        if (currentUser != null){
            log.setUserId(currentUser.getId());
        }
        log.setOperator(RequestContext.get(CURRENT_USERNAME, String.class, DEFAULT_USERNAME));
        log.setSuccess(ex == null);
        try{
            log.setParams(JsonUtils.toJson(jp.getArgs()));
        }catch (Exception e){
            log.setParams(e.getMessage());
        }
        try{
            log.setResult(JsonUtils.toJson(result));
        }catch (Exception e){
            log.setResult(e.getMessage());
        }
        log.setTimestamp(new Date(System.currentTimeMillis()));
        logService.save(log);
    }
} 