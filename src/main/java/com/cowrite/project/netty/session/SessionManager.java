package com.cowrite.project.netty.session;


import com.cowrite.project.netty.protocol.NettyMessage;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Session管理器
 */
@Component
public class SessionManager {

    //绑定channel和user
    public static final AttributeKey<String> USER_ID = AttributeKey.valueOf("userId");
    //绑定channel和docId
    public static final AttributeKey<String> DOC_ID = AttributeKey.valueOf("docId");

    //用来记录有哪些文档有缓存
    private List<String> cacheDoc = Collections.synchronizedList(new ArrayList<>());

    private Map<String, Map<String, UserSession>> sessions = new ConcurrentHashMap<>();

    //新增在线用户
    public void addSessions(String docId,String userId,Channel channel){
            sessions.computeIfAbsent(docId, k -> new HashMap<>())
                    .put(userId, new UserSession(userId, docId, channel));
        }

     //删除channel
    public  void deleteSessions(String docId,String userId) {
        if (docId == null || userId == null) {
            return; // 防止 null key
        }

        sessions.computeIfPresent(docId, (key, userSessions) -> {
            userSessions.remove(userId);

            // 如果当前文档已无用户，可以移除该文档的 map，节省内存
            if (userSessions.isEmpty()) {
                return null; // remove the entry
            }

            return userSessions;
        });
    }


    //查找用户channel
    public UserSession findSessions(String docId,String userId){
        return sessions.get(docId).get(userId);
    }

    //查找编辑文档的所有用户
    public Set<String> getOnlineUsers(String docId) {
        if (docId == null) {
            return Collections.emptySet();
        }

        Map<String, UserSession> userSessions = sessions.get(docId);
        if (userSessions == null) {
            return Collections.emptySet();
        }

        // 返回当前文档中所有用户的 ID 集合（不可变）
        return Collections.unmodifiableSet(userSessions.keySet());
    }

    //同步信息
    public void sendMessageToOthers(String docId, String excludeUserId, NettyMessage nettyMessage) {
        if (docId == null || excludeUserId == null || nettyMessage.getContent() == null) {
            return; // 参数检查
        }

        Map<String, UserSession> userSessions = sessions.get(docId);
        if (userSessions == null || userSessions.isEmpty()) {
            return; // 没有用户在线
        }

        for (Map.Entry<String, UserSession> entry : userSessions.entrySet()) {
            String userId = entry.getKey();
            UserSession session = entry.getValue();

            if (!userId.equals(excludeUserId)) {
                Channel channel = session.getChannel();
                if (channel != null && channel.isActive()) {
                    channel.writeAndFlush(nettyMessage);
                }
            }
        }
    }

    //全局发送
    public void broadcastToAll(String docId, Object message) {
        if (docId == null || message == null) {
            return;
        }

        Map<String, UserSession> userSessions = sessions.get(docId);
        if (userSessions == null || userSessions.isEmpty()) {
            return;
        }

        for (UserSession session : userSessions.values()) {
            Channel channel = session.getChannel();
            if (channel != null && channel.isActive()) {
                channel.writeAndFlush(message);
            }
        }
    }

    //获取所有用户session
    public Collection<UserSession> getAllUserSessions(String docId) {
        Map<String, UserSession> userSessions = sessions.get(docId);
        return userSessions == null ? Collections.emptyList() : userSessions.values();
    }

    //获取channel
    public Channel getChannel(String docId, String userId) {
        UserSession session = findSessions(docId, userId);
        return session == null ? null : session.getChannel();
    }

    //查看用户是否在线
    public boolean isUserOnline(String docId, String userId) {
        if (docId == null || userId == null) {
            return false;
        }

        Map<String, UserSession> userSessions = sessions.get(docId);
        return userSessions != null && userSessions.containsKey(userId);
    }

    // 获取当前缓存的文档Id
    public List<String> getAllCacheDocIds() {
        return cacheDoc;
    }

    //添加缓存文档
    public void putCacheDocId(String docId){
        cacheDoc.add(docId);
    }

    //清空缓存数组
    public void clearCacheDocId(){
        cacheDoc.clear();
    }


}