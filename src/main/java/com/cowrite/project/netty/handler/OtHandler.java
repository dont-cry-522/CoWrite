package com.cowrite.project.netty.handler;

import com.cowrite.project.netty.protocol.NettyMessage;
import org.checkerframework.checker.units.qual.N;

import java.util.ArrayList;
import java.util.List;

public class OtHandler {

    /**
     * 对两个操作进行转换，返回转换后的操作数组
     *
     * @param op1 用户A的操作
     * @param op2 用户B的操作
     * @return 转换后的操作数组，包含两个操作（可能为 null）
     */
    public static NettyMessage[] transform(NettyMessage op1, NettyMessage op2) {
        if (op1 == null || op2 == null) {
            throw new IllegalArgumentException("操作不能为 null");
        }

        String type1 = op1.getOperationType();
        String type2 = op2.getOperationType();

        if ("insert".equals(type1) && "insert".equals(type2)) {
            return transformInsertInsert(op1, op2);
        } else if ("insert".equals(type1) && "delete".equals(type2)) {
            return transformInsertDelete(op1, op2);
        } else if ("delete".equals(type1) && "insert".equals(type2)) {
            return transformDeleteInsert(op1, op2);
        } else if ("delete".equals(type1) && "delete".equals(type2)) {
            return transformDeleteDelete(op1, op2);
        }

        // 默认不转换
        return new NettyMessage[]{op1, op2};
    }

    // 插入 vs 插入：调整插入位置
    private static NettyMessage[] transformInsertInsert(NettyMessage op1, NettyMessage op2) {
        NettyMessage newOp1 = new NettyMessage(op1);
        NettyMessage newOp2 = new NettyMessage(op2);

        int index1 = newOp1.getIndex();
        int index2 = newOp2.getIndex();
        String content1 = newOp1.getContent();

        if (index1 < index2) {
            newOp2.setIndex(index2 + content1.length());
        } else if (index1 > index2) {
            newOp1.setIndex(index1 + op2.getContent().length());
        } else {
            // 位置相同，使用时间戳决定谁先插入
            if (op1.getTimestamp() < op2.getTimestamp()) {
                newOp2.setIndex(index2 + content1.length());
            } else {
                newOp1.setIndex(index1 + op2.getContent().length());
            }
        }

        return filterNull(newOp1, newOp2);
    }

    // 插入 vs 删除：删除位置可能需要调整
    private static NettyMessage[] transformInsertDelete(NettyMessage op1, NettyMessage op2) {
        NettyMessage newOp1 = new NettyMessage(op1);
        NettyMessage newOp2 = new NettyMessage(op2);

        int index1 = newOp1.getIndex();
        int index2 = newOp2.getIndex();

        if (index1 <= index2) {
            newOp2.setIndex(index2 + op1.getContent().length());
        }

        return filterNull(newOp1, newOp2);
    }

    // 删除 vs 插入：插入位置可能需要调整
    private static NettyMessage[] transformDeleteInsert(NettyMessage op1, NettyMessage op2) {
        NettyMessage newOp1 = new NettyMessage(op1);
        NettyMessage newOp2 = new NettyMessage(op2);

        int index1 = newOp1.getIndex();
        int index2 = newOp2.getIndex();

        if (index2 <= index1) {
            newOp1.setIndex(index1 + op2.getContent().length());
        }

        return filterNull(newOp1, newOp2);
    }

    // 删除 vs 删除：位置重叠时需要处理
    private static NettyMessage[] transformDeleteDelete(NettyMessage op1, NettyMessage op2) {
        NettyMessage newOp1 = new NettyMessage(op1);
        NettyMessage newOp2 = new NettyMessage(op2);

        int index1 = newOp1.getIndex();
        int index2 = newOp2.getIndex();

        if (index1 < index2) {
            // 不冲突，不做处理
        } else if (index1 > index2) {
            newOp1.setIndex(index1 - op2.getContent().length());
        } else {
            // 位置相同，保留时间戳较小的操作
            if (op1.getTimestamp() < op2.getTimestamp()) {
                newOp2 = null; // 忽略 op2
            } else {
                newOp1 = null; // 忽略 op1
            }
        }

        return filterNull(newOp1, newOp2);
    }

    /**
     * 过滤掉 null 的操作
     */
    private static NettyMessage[] filterNull(NettyMessage... messages) {
        List<NettyMessage> result = new ArrayList<>();
        for (NettyMessage msg : messages) {
            if (msg != null) {
                result.add(msg);
            }
        }
        return result.toArray(new NettyMessage[0]);
    }
}