package com.cowrite.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;

/**
 * Webhook 实体类
 * @author Hibiscus-code-generate
 */
@TableName("hib_webhook")
public class Webhook extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
    * 主键ID
    */
    @TableId
    private Long id;

    /**
    * 所属组织ID
    */
    @TableField("organization_id")
    private Long organizationId;

    /**
    * 事件类型（如：DOCUMENT_UPDATED）
    */
    @TableField("event_type")
    private String eventType;

    /**
    * Webhook接收地址
    */
    @TableField("target_url")
    private String targetUrl;

    /**
    * 是否启用
    */
    @TableField("enabled")
    private Boolean enabled;

    /**
    * 签名密钥（用于校验）
    */
    @TableField("secret")
    private String secret;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
    this.organizationId = organizationId;
    }
    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
    this.eventType = eventType;
    }
    public String getTargetUrl() {
    return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
    this.targetUrl = targetUrl;
    }
    public Boolean getEnabled() {
    return enabled;
    }

    public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
    }
    public String getSecret() {
    return secret;
    }

    public void setSecret(String secret) {
    this.secret = secret;
    }
}
