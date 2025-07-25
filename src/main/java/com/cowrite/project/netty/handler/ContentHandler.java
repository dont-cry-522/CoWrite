package com.cowrite.project.netty.handler;

import com.cowrite.project.netty.protocol.Cache;
import com.cowrite.project.netty.protocol.NettyMessage;
import com.cowrite.project.netty.session.SessionManager;
import com.cowrite.project.service.UserService;
import com.cowrite.project.utils.RedisUtils;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Resource;
import java.util.List;

public class ContentHandler {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private SessionManager sessionManager;

    @Resource
    private MongoTemplate mongoTemplate;

    public String applyOperations(String docId,List<NettyMessage> operations) {
        //从mongodb中查找内容
        String content = "1111111";
        StringBuilder newContent = new StringBuilder(content);

            for (NettyMessage msg : operations) {

                if ("insert".equals(msg.getOperationType())) {
                    newContent.insert(msg.getIndex(), msg.getContent());
                } else if ("delete".equals(msg.getOperationType())) {
                    int end = Math.min(msg.getIndex() + msg.getContent().length(), newContent.length());
                    newContent.delete(msg.getIndex(), end);
                }
            }
            content=newContent.toString();
            Cache cacheContent = new Cache(docId, content);
            contentCache(docId,cacheContent);
            return content;
    }

    public void contentCache(String docId,Object content){
        if (!sessionManager.getAllCacheDocIds().contains(docId)) {
            sessionManager.putCacheDocId(docId);
        }
         redisUtils.set("docCache"+docId,content);
    }

}
