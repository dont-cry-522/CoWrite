package com.cowrite.project.netty.protocol;

import java.util.HashMap;
import java.util.Map;

import static com.cowrite.project.utils.JsonUtils.fromJson;
import static com.cowrite.project.utils.JsonUtils.toJson;


/**
 * 消息基类
 */
public class NettyMessage {

    /**
     * 消息类型
     */
    private MessageType type;

    /**
     * 文档ID
     */
    private String docId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 消息时间戳
     */
    private long timestamp;

    /**
     * 消息负载
     */
    private Map<String, Object> payload = new HashMap<>();

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }

    public static class Builder {
        private final NettyMessage instance;

        public Builder() {
            instance = new NettyMessage();
        }

        public Builder type(MessageType type) {
            instance.type = type;
            return this;
        }

        public Builder docId(String docId) {
            instance.docId = docId;
            return this;
        }

        public Builder userId(String userId) {
            instance.userId = userId;
            return this;
        }

        public Builder timestamp(long timestamp) {
            instance.timestamp = timestamp;
            return this;
        }

        public Builder payload(Map<String, Object> payload) {
            instance.payload = payload;
            return this;
        }

        // 方便单条添加
        public Builder putPayload(String key, Object value) {
            instance.payload.put(key, value);
            return this;
        }

        public NettyMessage build() {
            // 如果timestamp没设，默认设置当前时间
            if (instance.timestamp == 0) {
                instance.timestamp = System.currentTimeMillis();
            }
            return instance;
        }
    }

    public String toJsonString() {
        return toJson(this);
    }

    public static NettyMessage fromJsonString(String json) {
        return fromJson(json, NettyMessage.class);
    }

    @Override
    public String toString() {
        return "NettyMessage{" +
                "type=" + type +
                ", docId='" + docId + '\'' +
                ", userId='" + userId + '\'' +
                ", timestamp=" + timestamp +
                ", payload=" + payload +
                '}';
    }
}
