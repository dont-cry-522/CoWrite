package com.cowrite.project.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cowrite.project.common.enums.NotificationLevel;
import com.cowrite.project.common.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通知实体类
 *
 * @author heathcetide
 */
@TableName("hib_notification")
public class Notification extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 接收用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 通知类型
     */
    @TableField("type")
    private NotificationType type;

    /**
     * 通知标题
     */
    @TableField("title")
    private String title;

    /**
     * 通知内容
     */
    @TableField("content")
    private String content;

    /**
     * 通知级别
     */
    @TableField("level")
    private NotificationLevel level;

    /**
     * 是否已读
     */
    @TableField("is_read")
    private boolean isRead;

    /**
     * 关联业务ID
     */
    @TableField("business_id")
    private String businessId;

    /**
     * 关联业务类型
     */
    @TableField("business_type")
    private String businessType;

    /**
     * 额外数据(JSON)
     */
    @TableField("extra_data")
    private String extraData;

    /**
     * 发送时间
     */
    @TableField("send_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendTime;

    /**
     * 读取时间
     */
    @TableField("read_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime readTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public NotificationLevel getLevel() {
        return level;
    }

    public void setLevel(NotificationLevel level) {
        this.level = level;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }

    public LocalDateTime getReadTime() {
        return readTime;
    }

    public void setReadTime(LocalDateTime readTime) {
        this.readTime = readTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Builder 静态内部类
    public static class Builder {
        private final Notification notification;

        public Builder() {
            notification = new Notification();
        }

        public Builder userId(Long userId) {
            notification.setUserId(userId);
            return this;
        }

        public Builder type(NotificationType type) {
            notification.setType(type);
            return this;
        }

        public Builder title(String title) {
            notification.setTitle(title);
            return this;
        }

        public Builder content(String content) {
            notification.setContent(content);
            return this;
        }

        public Builder level(NotificationLevel level) {
            notification.setLevel(level);
            return this;
        }

        public Builder read(boolean read) {
            notification.setRead(read);
            return this;
        }

        public Builder businessId(String businessId) {
            notification.setBusinessId(businessId);
            return this;
        }

        public Builder businessType(String businessType) {
            notification.setBusinessType(businessType);
            return this;
        }

        public Builder extraData(String extraData) {
            notification.setExtraData(extraData);
            return this;
        }

        public Builder sendTime(LocalDateTime sendTime) {
            notification.setSendTime(sendTime);
            return this;
        }

        public Builder readTime(LocalDateTime readTime) {
            notification.setReadTime(readTime);
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            notification.setCreatedAt(createdAt);
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            notification.setUpdatedAt(updatedAt);
            return this;
        }
        public Notification build() {
            return notification;
        }
    }


}