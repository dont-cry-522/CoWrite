package com.cowrite.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Plugin 实体类
 * @author Hibiscus-code-generate
 */
@TableName("hib_plugins")
public class Plugin extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 插件名称
     */
    @TableField("plugin_name")
    private String pluginName;

    /**
     * 插件类型（如：文本生成、图像识别等）
     */
    @TableField("plugin_type")
    private String pluginType;

    /**
     * 插件描述
     */
    @TableField("description")
    private String description;

    /**
     * 插件版本
     */
    @TableField("version")
    private String version;

    /**
     * 插件的 Webhook 回调地址
     */
    @TableField("webhook_url")
    private String webhookUrl;

    /**
     * 插件API地址
     */
    @TableField("api_url")
    private String apiUrl;

    /**
     * 插件文档地址
     */
    @TableField("documentation_url")
    private String documentationUrl;

    /**
     * 插件作者
     */
    @TableField("author")
    private String author;

    /**
     * 插件是否启用
     */
    @TableField("enabled")
    private Boolean enabled;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getPluginType() {
        return pluginType;
    }

    public void setPluginType(String pluginType) {
        this.pluginType = pluginType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getDocumentationUrl() {
        return documentationUrl;
    }

    public void setDocumentationUrl(String documentationUrl) {
        this.documentationUrl = documentationUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "Plugin{" +
                "id=" + id +
                ", pluginName='" + pluginName + '\'' +
                ", pluginType='" + pluginType + '\'' +
                ", description='" + description + '\'' +
                ", version='" + version + '\'' +
                ", webhookUrl='" + webhookUrl + '\'' +
                ", apiUrl='" + apiUrl + '\'' +
                ", documentationUrl='" + documentationUrl + '\'' +
                ", author='" + author + '\'' +
                ", enabled=" + enabled +
                '}';
    }

    /**
     * Builder 模式
     */
    public static class Builder {
        private Long id;
        private String pluginName;
        private String pluginType;
        private String description;
        private String version;
        private String webhookUrl;
        private String apiUrl;
        private String documentationUrl;
        private String author;
        private Boolean enabled;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setPluginName(String pluginName) {
            this.pluginName = pluginName;
            return this;
        }

        public Builder setPluginType(String pluginType) {
            this.pluginType = pluginType;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setVersion(String version) {
            this.version = version;
            return this;
        }

        public Builder setWebhookUrl(String webhookUrl) {
            this.webhookUrl = webhookUrl;
            return this;
        }

        public Builder setApiUrl(String apiUrl) {
            this.apiUrl = apiUrl;
            return this;
        }

        public Builder setDocumentationUrl(String documentationUrl) {
            this.documentationUrl = documentationUrl;
            return this;
        }

        public Builder setAuthor(String author) {
            this.author = author;
            return this;
        }

        public Builder setEnabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Plugin build() {
            Plugin plugin = new Plugin();
            plugin.setId(this.id);
            plugin.setPluginName(this.pluginName);
            plugin.setPluginType(this.pluginType);
            plugin.setDescription(this.description);
            plugin.setVersion(this.version);
            plugin.setWebhookUrl(this.webhookUrl);
            plugin.setApiUrl(this.apiUrl);
            plugin.setDocumentationUrl(this.documentationUrl);
            plugin.setAuthor(this.author);
            plugin.setEnabled(this.enabled);
            return plugin;
        }
    }
}
