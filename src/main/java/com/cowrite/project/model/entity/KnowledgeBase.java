package com.cowrite.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;

/**
 * 知识库实体类， hib_knowledge_base。
 * @author heathcetide
 */
@TableName("hib_knowledge_base")
public class KnowledgeBase extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 知识库名称
     */
    @TableField("name")
    private String name;

    /**
     * 知识库描述
     */
    @TableField("description")
    private String description;

    /**
     * 知识库类型（如：document、faq、qa等）
     */
    @TableField("type")
    private String type;

    /**
     * 知识库所属用户ID（个人知识库）
     */
    @TableField("owner_id")
    private Long ownerId;

    /**
     * 组织ID（如果是组织知识库）
     */
    @TableField("organization_id")
    private Long organizationId;

    /**
     * 知识库封面图URL
     */
    @TableField("cover_url")
    private String coverUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    @Override
    public String toString() {
        return "KnowledgeBase{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", ownerId=" + ownerId +
                ", organizationId=" + organizationId +
                ", coverUrl='" + coverUrl + '\'' +
                '}';
    }
}
