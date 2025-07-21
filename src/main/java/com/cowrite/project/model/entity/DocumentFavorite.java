package com.cowrite.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DocumentFavorite 实体类
 * @author Hibiscus-code-generate
 */
@TableName("hib_document_favorite")
public class DocumentFavorite extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
    * 主键ID
    */
    @TableId
    private Long id;

    /**
    * 用户ID
    */
    @TableField("user_id")
    private Long userId;

    /**
    * 被收藏的文档ID
    */
    @TableField("document_id")
    private Long documentId;

    /**
    * 收藏时间
    */
    @TableField("favorited_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime favoritedAt;


    public Long getId() {
    return id;
    }

    public void setId(Long id) {
    this.id = id;
    }
    public Long getUserId() {
    return userId;
    }

    public void setUserId(Long userId) {
    this.userId = userId;
    }
    public Long getDocumentId() {
    return documentId;
    }

    public void setDocumentId(Long documentId) {
    this.documentId = documentId;
    }
    public LocalDateTime getFavoritedAt() {
    return favoritedAt;
    }

    public void setFavoritedAt(LocalDateTime favoritedAt) {
    this.favoritedAt = favoritedAt;
    }
}
