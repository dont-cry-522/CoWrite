package com.cowrite.project.netty.protocol;

public enum MessageType {
    DOCUMENT_JOIN("加入文档"),
    DOCUMENT_LEAVE("离开文档"),
    DOCUMENT_UPDATE("文档更新"),
    DOCUMENT_CHUNK("文档分片"),

    MOUSE_MOVE("移动鼠标"),
    CONTENT_INSERT("插入内容"),
    CONTENT_DELETE("删除内容"),
    CONTENT_REPLACE("替换内容"),

    CURSOR_MOVE("移动光标"),
    CURSOR_SELECTION("选择范围"),

    USER_ONLINE("用户上线"),
    USER_OFFLINE("用户下线"),
    USER_TYPING("用户正在输入"),

    SYSTEM_NOTICE("系统通知"),
    ERROR("错误消息"),
    UNDO("撤销操作"),
    REDO("重做操作"),
    PERMISSION_CHANGE("权限变更"),
    SAVE_VERSION("保存版本"),
    LOAD_VERSION("加载版本"),
    LOCK("锁定文档"),
    UNLOCK("解锁文档"),
    CHUNK_REQUEST("请求分块"),
    HEARTBEAT("心跳消息");  // 心跳消息

    private final String description;

    MessageType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
