package com.cowrite.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;

/**
 * DocumentTemplate 实体类
 * @author Hibiscus-code-generate
 */
@TableName("hib_document_template")
public class DocumentTemplate extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
    * 主键ID
    */
    @TableId
    private Long id;

    /**
    * 模板标题
    */
    @TableField("title")
    private String title;

    /**
    * 模板描述
    */
    @TableField("description")
    private String description;

    /**
    * 模板内容
    */
    @TableField("content")
    private String content;

    /**
    * 创建人ID
    */
    @TableField("creator_id")
    private Long creatorId;

    /**
    * 模板可见性（SYSTEM / PUBLIC / PRIVATE）
    */
    @TableField("scope")
    private String scope;

    public Long getId() {
    return id;
    }

    public void setId(Long id) {
    this.id = id;
    }
    public String getTitle() {
    return title;
    }

    public void setTitle(String title) {
    this.title = title;
    }
    public String getDescription() {
    return description;
    }

    public void setDescription(String description) {
    this.description = description;
    }
    public String getContent() {
    return content;
    }

    public void setContent(String content) {
    this.content = content;
    }
    public Long getCreatorId() {
    return creatorId;
    }

    public void setCreatorId(Long creatorId) {
    this.creatorId = creatorId;
    }
    public String getScope() {
    return scope;
    }

    public void setScope(String scope) {
    this.scope = scope;
    }
}
