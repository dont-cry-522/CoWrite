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
@TableName("hib_document_tag")
public class DocumentTag extends BaseEntity implements Serializable {

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
}
