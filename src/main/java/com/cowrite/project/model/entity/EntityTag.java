package com.cowrite.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;


/**
 * DocumentTag 实体类
 * @author Hibiscus-code-generate
 */
@TableName("hib_entity_tag")
public class EntityTag extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
    * 主键ID
    */
    @TableId
    private Long id;
    /**
     * 实体类型
     */
    @TableField("entity_type")
    private Long entityType;
    /**
    * 实体ID
    */
    @TableField("entity_id")
    private Long documentId;

    /**
    * 标签ID
    */
    @TableField("tag_id")
    private Long tagId;


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
    public Long getTagId() {
    return tagId;
    }

    public void setTagId(Long tagId) {
    this.tagId = tagId;
    }

    public Long getEntityType() {
        return entityType;
    }

    public void setEntityType(Long entityType) {
        this.entityType = entityType;
    }
}
