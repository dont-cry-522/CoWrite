package com.cowrite.project.netty.session;


import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Session管理器
 */
public class SessionManager {

    /**
     * key: docId, value: Map<userId, UserSession>
     */
    private static final Map<String, Map<String, UserSession>> sessions = new ConcurrentHashMap<>();

    /**
     * 添加用户会话
     */
    public void addSession(String userId, String docId, Channel channel) {
        sessions.computeIfAbsent(docId, k -> new ConcurrentHashMap<>())
                .put(userId, new UserSession(userId, docId, channel));
    }

    /**
     * 删除用户会话
     */
    public void removeSession(String userId, String docId) {
        Map<String, UserSession> userSessions = sessions.get(docId);
        if (userSessions != null) {
            userSessions.remove(userId);
            if (userSessions.isEmpty()) {
                sessions.remove(docId);
            }
        }
    }

    /**
     * 获取用户会话
     */
    public UserSession getSession(String userId, String docId) {
        Map<String, UserSession> userSessions = sessions.get(docId);
        if (userSessions != null) {
            return userSessions.get(userId);
        }
        return null;
    }

    /**
     * 判断用户是否在线（在某文档中）
     */
    public boolean isUserOnline(String userId, String docId) {
        Map<String, UserSession> userSessions = sessions.get(docId);
        return userSessions != null && userSessions.containsKey(userId);
    }

    /**
     * 广播消息到指定文档所有用户
     */
    public void broadcastToDocument(String docId, String message) {
        Map<String, UserSession> userSessions = sessions.get(docId);
        if (userSessions == null) return;

        for (UserSession session : userSessions.values()) {
            Channel channel = session.getChannel();
            if (channel.isActive()) {
                channel.writeAndFlush(new TextWebSocketFrame(message));
            }
        }
    }

    /**
     * 广播消息给指定用户（所有文档会话）
     */
    public void broadcastToUser(String userId, String message) {
        for (Map<String, UserSession> userSessions : sessions.values()) {
            UserSession session = userSessions.get(userId);
            if (session != null && session.getChannel().isActive()) {
                session.getChannel().writeAndFlush(new TextWebSocketFrame(message));
            }
        }
    }

    /**
     * 获取指定文档的所有在线用户ID列表
     */
    public List<String> getOnlineUsers(String docId) {
        Map<String, UserSession> userSessions = sessions.get(docId);
        if (userSessions == null) return Collections.emptyList();
        return new ArrayList<>(userSessions.keySet());
    }

    /**
     * 获取指定用户的所有文档ID列表
     */
    public List<String> getUserDocuments(String userId) {
        List<String> docs = new ArrayList<>();
        for (Map.Entry<String, Map<String, UserSession>> entry : sessions.entrySet()) {
            if (entry.getValue().containsKey(userId)) {
                docs.add(entry.getKey());
            }
        }
        return docs;
    }

    /**
     * 获取用户在某文档的Channel
     */
    public Channel getUserChannel(String userId, String docId) {
        UserSession session = getSession(userId, docId);
        return session != null ? session.getChannel() : null;
    }
}