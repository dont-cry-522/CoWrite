package com.cowrite.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * KnowledgeBaseCollaborator 实体类
 * @author Hibiscus-code-generate
 */
@TableName("hib_knowledge_base_collaborator")
public class KnowledgeBaseCollaborator extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
    * 主键ID
    */
    @TableId
    private Long id;

    /**
    * 所属知识库ID
    */
    @TableField("knowledge_base_id")
    private Long knowledgeBaseId;

    /**
    * 协作者用户ID
    */
    @TableField("user_id")
    private Long userId;

    /**
    * 角色（VIEWER / EDITOR / ADMIN）
    */
    @TableField("role")
    private String role;

    /**
    * 加入时间
    */
    @TableField("joined_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime joinedAt;


    public Long getId() {
    return id;
    }

    public void setId(Long id) {
    this.id = id;
    }
    public Long getKnowledgeBaseId() {
    return knowledgeBaseId;
    }

    public void setKnowledgeBaseId(Long knowledgeBaseId) {
    this.knowledgeBaseId = knowledgeBaseId;
    }
    public Long getUserId() {
    return userId;
    }

    public void setUserId(Long userId) {
    this.userId = userId;
    }
    public String getRole() {
    return role;
    }

    public void setRole(String role) {
    this.role = role;
    }
    public LocalDateTime getJoinedAt() {
    return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
    this.joinedAt = joinedAt;
    }
}
