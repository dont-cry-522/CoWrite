package com.cowrite.project.netty.protocol;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static com.cowrite.project.utils.JsonUtils.fromJson;
import static com.cowrite.project.utils.JsonUtils.toJson;


/**
 * 消息基类
 */
public class NettyMessage {

    //操作类型
    private String operationType;
    //文档ID
    private String docId;
    //用户Id
    private String userId;
    //修改内容
    private String content;
    //修改位置
    private Integer index;
    //修改时间
    private Long timestamp;
    //操作次数
    private Integer operationNum;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Integer getOperationNum() {
        return operationNum;
    }

    public void setOperationNum(Integer operationNum) {
        this.operationNum = operationNum;
    }

    public void settimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public NettyMessage(String operationType, String docId, String userId, String content, Integer index, Long timestamp,Integer operationNum) {
        this.operationType = operationType;
        this.docId = docId;
        this.userId = userId;
        this.content = content;
        this.index = index;
        this.timestamp = timestamp;
        this.operationNum=operationNum;
    }

    public NettyMessage(NettyMessage op) {
        this.operationType = op.getOperationType();
        this.docId = op.getDocId();
        this.userId = op.getUserId();
        this.content = op.getContent();
        this.index = op.getIndex();
        this.timestamp = op.getTimestamp();
        this.operationNum= op.getOperationNum();
    }

    private static final ObjectMapper mapper = new ObjectMapper();

    public static NettyMessage fromJson(String json) {
        try {
            return mapper.readValue(json, NettyMessage.class);
        } catch (Exception e) {
            throw new RuntimeException("JSON 解析失败: " + json, e);
        }
    }

    public String toJson() {
        try {
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException("序列化失败", e);
        }
    }




}
