package com.cowrite.project.netty.session;


import io.netty.channel.Channel;

import java.util.Objects;

/**
 * 用户会话
 */
public class UserSession {

    /**
     * 用户ID
     */
    private final String userId;

    /**
     * 文档ID
     */
    private final String docId;

    /**
     * 用户通道
     */
    private final Channel channel;

    public UserSession(String userId, String docId, Channel channel) {
        this.userId = userId;
        this.docId = docId;
        this.channel = channel;
    }

    public String getUserId() {
        return userId;
    }

    public String getDocId() {
        return docId;
    }

    public Channel getChannel() {
        return channel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSession that = (UserSession) o;
        return userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}