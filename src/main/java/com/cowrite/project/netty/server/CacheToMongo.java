package com.cowrite.project.netty.server;


import com.cowrite.project.netty.session.SessionManager;
import com.cowrite.project.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class CacheToMongo {

    private static final Logger log = LoggerFactory.getLogger(CacheToMongo.class);
    @Resource
    public SessionManager sessionManager;

    @Resource
    public RedisUtils redisUtils;

    @Scheduled(fixedRate = 60000) // 每 60 秒执行一次
    public void syncCacheToMongoDB() {
        List<String> docs = sessionManager.getAllCacheDocIds();
        for (String docId : docs) {
            Object content = redisUtils.get("docCache" + docId);
            log.info("1111");
            //TODO
            //将内容放进mongodb
        }
        sessionManager.clearCacheDocId();
    }
}
