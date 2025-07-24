package com.cowrite.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 组织成员实体类 hib_organization_member。
 */
@TableName("hib_organization_member")
public class OrganizationMember extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    @TableId
    private Long id;

    /**
     * 所属组织的ID
     */
    @TableField("organization_id")
    private Long organizationId;

    /**
     * 成员用户的ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 成员在组织中的角色（如：owner、admin、member）
     */
    @TableField("role")
    private String role;

    /**
     * 成员状态（如：active、pending、removed）
     */
    @TableField("status")
    private String status;

    /**
     * 加入组织的时间
     */
    @TableField("joined_at")
    private LocalDateTime joinedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public String toString() {
        return "OrganizationMember{" +
                "id=" + id +
                ", organizationId=" + organizationId +
                ", userId=" + userId +
                ", role='" + role + '\'' +
                ", status='" + status + '\'' +
                ", joinedAt=" + joinedAt +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long organizationId;
        private Long userId;
        private String role;
        private String status;
        private LocalDateTime joinedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder organizationId(Long organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder joinedAt(LocalDateTime joinedAt) {
            this.joinedAt = joinedAt;
            return this;
        }

        public OrganizationMember build() {
            OrganizationMember member = new OrganizationMember();
            member.setId(this.id);
            member.setOrganizationId(this.organizationId);
            member.setUserId(this.userId);
            member.setRole(this.role);
            member.setStatus(this.status);
            member.setJoinedAt(this.joinedAt);
            return member;
        }
    }

}
