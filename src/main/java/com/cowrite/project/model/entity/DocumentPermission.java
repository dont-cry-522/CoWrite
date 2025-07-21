package com.cowrite.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DocumentPermission 实体类
 * @author Hibiscus-code-generate
 */
@TableName("hib_document_permission")
public class DocumentPermission extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
    * 主键ID
    */
    @TableId
    private Long id;

    /**
    * 文档ID
    */
    @TableField("document_id")
    private Long documentId;

    /**
    * 被授权用户ID
    */
    @TableField("user_id")
    private Long userId;

    /**
    * 权限类型（VIEW / EDIT / COMMENT / ADMIN）
    */
    @TableField("permission")
    private String permission;

    /**
    * 授权人ID
    */
    @TableField("granted_by")
    private Long grantedBy;

    /**
    * 授权时间
    */
    @TableField("granted_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime grantedAt;


    public Long getId() {
    return id;
    }

    public void setId(Long id) {
    this.id = id;
    }
    public Long getDocumentId() {
    return documentId;
    }

    public void setDocumentId(Long documentId) {
    this.documentId = documentId;
    }
    public Long getUserId() {
    return userId;
    }

    public void setUserId(Long userId) {
    this.userId = userId;
    }
    public String getPermission() {
    return permission;
    }

    public void setPermission(String permission) {
    this.permission = permission;
    }
    public Long getGrantedBy() {
    return grantedBy;
    }

    public void setGrantedBy(Long grantedBy) {
    this.grantedBy = grantedBy;
    }
    public LocalDateTime getGrantedAt() {
    return grantedAt;
    }

    public void setGrantedAt(LocalDateTime grantedAt) {
    this.grantedAt = grantedAt;
    }
}
