package com.cowrite.project.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

@Intercepts({
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
    @Signature(type = Executor.class, method = "query", 
              args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class SqlExecuteTimeInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(SqlExecuteTimeInterceptor.class);
    private static final long SLOW_SQL_THRESHOLD = 1000;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        String sqlId = mappedStatement.getId();
        long start = System.currentTimeMillis();
        
        try {
            return invocation.proceed();
        } finally {
            long cost = System.currentTimeMillis() - start;
            if (cost > SLOW_SQL_THRESHOLD) {
                logger.warn("[Slow SQL] ID: {} - {}ms", sqlId, cost);
            } else {
                logger.debug("[SQL] ID: {} - {}ms", sqlId, cost);
            }
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}