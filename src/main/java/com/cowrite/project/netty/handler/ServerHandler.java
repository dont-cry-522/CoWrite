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

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.cowrite.project.common.enums.ResponseCodeEnum.VALIDATION_ERROR;
import static com.cowrite.project.netty.protocol.MessageType.*;


public class ServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final Logger log = LoggerFactory.getLogger(ServerHandler.class);

    @Resource
    private SessionManager sessionManager;

    private HashMap<String,ArrayList<NettyMessage>> operationHistory = new HashMap<>();


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
       log.info("客户端连接建立: " + ctx.channel().remoteAddress());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame msg) throws Exception {
        // 获取 JSON 字符串
        String json = msg.text();

        // 解析成 NettyMessage 对象
        NettyMessage message = NettyMessage.fromJson(json);

        channelHandlerContext.channel().attr(SessionManager.DOC_ID).set(message.getDocId());
        channelHandlerContext.channel().attr(SessionManager.USER_ID).set(message.getUserId());
        sessionManager.addSessions(message.getDocId(), message.getUserId(),channelHandlerContext.channel());

        // 传给 handleMessage 方法处理
        handleMessage(channelHandlerContext, message);
    }

    private void handleMessage(ChannelHandlerContext ctx, NettyMessage message) {
        ContentHandler contentHandler = new ContentHandler();
        OtHandler ot = new OtHandler();
        String docId = message.getDocId();
        String userId = message.getUserId();

        if (operationHistory.get(docId) == null) {
            // 第一次操作：创建历史
            ArrayList<NettyMessage> history = new ArrayList<>();
            history.add(message);
            operationHistory.put(docId, history);

            // 直接广播原始操作
            sessionManager.sendMessageToOthers(docId, userId, message);

        } else {
            // 获取已有历史
            ArrayList<NettyMessage> history = operationHistory.get(message.getDocId());
            ArrayList<NettyMessage> newHistory = new ArrayList<>(history);
            NettyMessage transformedMessage = message;

            // OT 转换所有历史操作
            for (int i = 0; i < history.size(); i++) {
                NettyMessage oldOp = history.get(i);
                NettyMessage[] transformed = ot.transform(oldOp, message);
                newHistory.set(i, transformed[0]); // 更新历史操作
                transformedMessage = transformed[1]; // 更新当前操作
            }

            newHistory.add(transformedMessage);

            operationHistory.put(docId, newHistory);

            sessionManager.sendMessageToOthers(docId, userId, transformedMessage);

            if (newHistory.size() >= 100) {
                String currentContent = contentHandler.applyOperations(docId,newHistory);
                newHistory.clear();
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        String userId = ctx.channel().attr(SessionManager.USER_ID).get();
        String docId = ctx.channel().attr(SessionManager.DOC_ID).get();
        sessionManager.deleteSessions(docId,userId);
        // 2. 解绑 Channel 上的属性
        ctx.attr(SessionManager.USER_ID).set(null);
        // 继续传播事件
        ctx.fireChannelInactive();
    }
}

