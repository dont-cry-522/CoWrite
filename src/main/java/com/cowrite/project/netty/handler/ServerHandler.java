package com.cowrite.project.netty.handler;

import com.cowrite.project.exception.BusinessException;
import com.cowrite.project.netty.protocol.MessageType;
import com.cowrite.project.netty.protocol.NettyMessage;
import com.cowrite.project.netty.session.SessionManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.cowrite.project.common.enums.ResponseCodeEnum.VALIDATION_ERROR;
import static com.cowrite.project.netty.protocol.MessageType.*;


public class ServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final Logger log = LoggerFactory.getLogger(ServerHandler.class);

    private final SessionManager sessionManager = new SessionManager();

    // 操作历史
    private final Map<String, List<NettyMessage>> documentOperationsHistory = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        NettyMessage message = NettyMessage.fromJsonString(text);
        System.out.print(message);
        try {
            handleMessage(channelHandlerContext, message);
        } catch (Exception e) {
            log.error("处理消息失败: " + e.getMessage());
            channelHandlerContext.writeAndFlush(new TextWebSocketFrame("错误: " + e.getMessage()));
            throw new BusinessException(VALIDATION_ERROR.code(), "处理消息失败: " + e.getMessage());
        }
    }

    private void handleMessage(ChannelHandlerContext ctx, NettyMessage message) {
        switch (message.getType()) {
            case DOCUMENT_JOIN:
                System.out.println(DOCUMENT_JOIN.getDescription());
                handleDocumentJoin(ctx, message);
                break;
            case DOCUMENT_LEAVE:
                System.out.println(DOCUMENT_LEAVE.getDescription());
                handleDocumentLeave(message);
                break;
            case MOUSE_MOVE:
                System.out.println(MOUSE_MOVE.getDescription());
                handleMouseMove(message);
                break;
            case CONTENT_INSERT:
                System.out.println(CONTENT_INSERT.getDescription());
                handleContentInsert(message);
                break;
            case CONTENT_DELETE:
                System.out.println(CONTENT_DELETE.getDescription());
//                handleContentDelete(message);
                break;
            case CURSOR_MOVE:
                System.out.println(CURSOR_MOVE.getDescription());
//                handleCursorMove(message);
                break;
            case UNDO:
                System.out.println(UNDO.getDescription());
//                handleUndo(message);
                break;
            case REDO:
                System.out.println(REDO.getDescription());
//                handleRedo(message);
                break;
            case PERMISSION_CHANGE:
                System.out.println(PERMISSION_CHANGE.getDescription());
//                handlePermissionChange(message);
                break;
            case SAVE_VERSION:
                System.out.println(SAVE_VERSION.getDescription());
//                handleSaveVersion(message);
                break;
            case LOAD_VERSION:
                System.out.println(LOAD_VERSION.getDescription());
//                handleLoadVersion(message);
                break;
            case LOCK:
                System.out.println(LOCK.getDescription());
//                handleLock(message);
                break;
            case UNLOCK:
                System.out.println(UNLOCK.getDescription());
//                handleUnlock(message);
                break;
            case CHUNK_REQUEST:
                System.out.println(CHUNK_REQUEST.getDescription());
//                handleChunkRequest(message);
                break;
            case HEARTBEAT:
                System.out.println(HEARTBEAT.getDescription());
                break;
            default:
                sendError(message.getUserId(), "未知的消息类型");
        }
    }

    private void handleMouseMove(NettyMessage message) {
        // 检查读权限 permissionService.checkPermission

        NettyMessage notice = new NettyMessage.Builder()
                .type(MOUSE_MOVE)
                .docId(message.getDocId())
                .userId(message.getUserId())
                .build();
        notice.getPayload().put("position", message.getPayload().get("position"));
        sessionManager.broadcastToDocument(message.getDocId(), notice.toJsonString());
    }

    /**
     * 处理内容插入
     */
    private void handleContentInsert(NettyMessage message) {
        String docId = message.getDocId();
        List<NettyMessage> history = documentOperationsHistory.getOrDefault(docId, new ArrayList<>());

        // 输出历史记录，供调试用
        for (NettyMessage nettyMessage : history) {
            System.out.println(nettyMessage);
        }

        // 对插入操作进行转换（处理冲突和位置调整）
        NettyMessage transformedOp = transformInsert(message, history);

        // 将转换后的操作添加到历史中
        history.add(transformedOp);
        documentOperationsHistory.put(docId, history);

        // 广播操作给所有在线用户
        broadcastOperationToDocument(docId, transformedOp);
    }


    // 插入操作转换
    private NettyMessage transformInsert(NettyMessage insertOp, List<NettyMessage> history) {
        Map<String, Object> insertPosition = (Map<String, Object>) insertOp.getPayload().get("position");
        List<Integer> insertPath = (List<Integer>) insertPosition.get("path");
        int insertOffset = (int) insertPosition.get("offset");
        String insertContent = (String) insertOp.getPayload().get("content");

        for (NettyMessage otherOp : history) {
            if (otherOp.getType() == MessageType.CONTENT_INSERT) {
                Map<String, Object> otherPosition = (Map<String, Object>) otherOp.getPayload().get("position");
                List<Integer> otherPath = (List<Integer>) otherPosition.get("path");
                int otherOffset = (int) otherPosition.get("offset");
                String otherContent = (String) otherOp.getPayload().get("content");

                // 如果路径相同或属于同一分支
                if (isSameOrAncestorPath(insertPath, otherPath)) {
                    if (insertOffset >= otherOffset) {
                        insertOffset += otherContent.length();
                    }
                }
            }
        }

        insertPosition.put("offset", insertOffset);
        insertOp.getPayload().put("position", insertPosition);
        insertOp.getPayload().put("content", insertContent);

        return insertOp;
    }

    private boolean isSameOrAncestorPath(List<Integer> pathA, List<Integer> pathB) {
        if (pathA.size() > pathB.size()) return false;
        for (int i = 0; i < pathA.size(); i++) {
            if (!pathA.get(i).equals(pathB.get(i))) {
                return false;
            }
        }
        return true;
    }

    // 删除操作转换
    private NettyMessage transformDelete(NettyMessage deleteOp, List<NettyMessage> history) {
        for (NettyMessage otherOp : history) {
            if (otherOp.getType() == MessageType.CONTENT_INSERT) {
                int position = (int) otherOp.getPayload().get("position");
                String content = (String) otherOp.getPayload().get("content");
                if ((int)deleteOp.getPayload().get("position") >= position) {
                    // 调整删除位置
                    deleteOp.getPayload().put("position", (int) deleteOp.getPayload().get("position") - content.length());
                }
            }
        }
        return deleteOp;
    }

    // 广播操作
    private void broadcastOperationToDocument(String docId, NettyMessage operation) {
        List<String> userIds = sessionManager.getOnlineUsers(docId);
        userIds.remove(operation.getUserId());
        for (String userId : userIds) {
            sessionManager.broadcastToUser(userId, operation.toJsonString());
        }
    }

    /**
     * 处理用户加入文档
     */
    private void handleDocumentJoin(ChannelHandlerContext ctx, NettyMessage message) {
        // 检查读权限 Permission Service

        // 添加用户会话
        sessionManager.addSession(message.getUserId(), message.getDocId(), ctx.channel());

        List<String> onlineUsers = sessionManager.getOnlineUsers(message.getDocId());

        NettyMessage userListNotice = new NettyMessage.Builder()
                .type(USER_ONLINE)
                .docId(message.getDocId())
                .userId("system")
                .timestamp(System.currentTimeMillis())
                .build();

        // payload里放整个在线用户列表
        userListNotice.getPayload().put("users", onlineUsers);
        // 一次性发送给新用户
        sessionManager.broadcastToUser(message.getUserId(), userListNotice.toJsonString());
    }

    private void handleDocumentLeave(NettyMessage message){
        sessionManager.removeSession(message.getUserId(), message.getDocId());
        List<String> onlineUsers = sessionManager.getOnlineUsers(message.getDocId());

        NettyMessage userListNotice = new NettyMessage.Builder()
                .type(USER_OFFLINE)
                .docId(message.getDocId())
                .userId("system")
                .timestamp(System.currentTimeMillis())
                .build();

        // payload里放整个在线用户列表
        userListNotice.getPayload().put("users", onlineUsers);
        // 一次性发送给新用户
        sessionManager.broadcastToUser(message.getUserId(), userListNotice.toJsonString());
    }

    private void sendError(String userId, String errorMessage) {
        try {
            System.out.println("发送错误消息给用户: " + userId);
            System.err.println("发送错误消息失败: " + errorMessage);
            // 构造错误消息
            NettyMessage error = new NettyMessage.Builder()
                    .type(ERROR)
                    .userId("system")
                    .timestamp(System.currentTimeMillis())
                    .build();
            error.getPayload().put("message", errorMessage);

            // 发送错误消息给指定用户
            sessionManager.broadcastToUser(userId, error.toJsonString());
        } catch (Exception e) {
            System.err.println("发送错误消息失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
