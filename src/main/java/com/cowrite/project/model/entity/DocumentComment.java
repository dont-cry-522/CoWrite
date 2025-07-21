package com.cowrite.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;

/**
 * DocumentComment 实体类
 * @author Hibiscus-code-generate
 */
@TableName("hib_document_comment")
public class DocumentComment extends BaseEntity implements Serializable {

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
    * 评论者ID
    */
    @TableField("user_id")
    private Long userId;

    /**
    * 评论内容
    */
    @TableField("content")
    private String content;

    /**
    * 锚点位置（块索引、偏移等）
    */
    @TableField("anchor")
    private String anchor;

    /**
    * 状态（ACTIVE / RESOLVED / DELETED）
    */
    @TableField("status")
    private String status;

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
    public String getContent() {
    return content;
    }

    public void setContent(String content) {
    this.content = content;
    }
    public String getAnchor() {
    return anchor;
    }

    public void setAnchor(String anchor) {
    this.anchor = anchor;
    }
    public String getStatus() {
    return status;
    }

    public void setStatus(String status) {
    this.status = status;
    }
}
