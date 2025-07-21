package com.cowrite.project.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerformanceAspect {

    private static final Logger perfLogger = LoggerFactory.getLogger("PERF");

    private static final long WARN_THRESHOLD = 500;

    @Around("execution(* com.cowrite.project.controller..*(..))")
    public Object monitor(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return pjp.proceed();
        } finally {
            long cost = System.currentTimeMillis() - start;
            String method = pjp.getSignature().toShortString();
            perfLogger.info("{} - {}ms", method, cost);
            if (cost > WARN_THRESHOLD) {
                perfLogger.warn("慢接口警告: {}", method);
            }
        }
    }
}