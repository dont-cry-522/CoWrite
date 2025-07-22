package com.cowrite.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;

/**
 * 组织实体类 hib_organization。
 */
@TableName("hib_organization")
public class Organization extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    @TableId
    private Long id;

    /**
     * 组织名称
     */
    @TableField("name")
    private String name;

    /**
     * 组织描述
     */
    @TableField("description")
    private String description;

    /**
     * 组织拥有者的用户ID
     */
    @TableField("owner_id")
    private Long ownerId;

    /**
     * 组织状态（如：active、disabled）
     */
    @TableField("status")
    private String status;

    /**
     * 是否公开发布（如：yes/no 或 1/0，视数据库设计而定）
     */
    @TableField("published")
    private String published;

    /**
     * 最大成员数量限制
     */
    @TableField("max_members")
    private Integer maxMembers;

    /**
     * 当前成员数量
     */
    @TableField("current_members")
    private Long currentMembers;

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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public Integer getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(Integer maxMembers) {
        this.maxMembers = maxMembers;
    }

    public Long getCurrentMembers() {
        return currentMembers;
    }

    public void setCurrentMembers(Long currentMembers) {
        this.currentMembers = currentMembers;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", ownerId=" + ownerId +
                ", status='" + status + '\'' +
                ", published='" + published + '\'' +
                ", maxMembers=" + maxMembers +
                ", currentMembers=" + currentMembers +
                '}';
    }
}
