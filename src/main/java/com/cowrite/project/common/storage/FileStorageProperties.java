package com.cowrite.project.common.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件存储配置
 */
@Component
@ConfigurationProperties(prefix = "cowrite.storage")
public class FileStorageProperties {

    /**
     * 存储类型：local 或 minio
     */
    private String type;

    // MinIO

    /**
     * MinIO 端点
     */
    private String minioEndpoint;

    /**
     * MinIO 访问密钥
     */
    private String minioAccessKey;

    /**
     * MinIO 密钥
     */
    private String minioSecretKey;

    /**
     * MinIO 存储桶
     */
    private String minioBucket;

    // Local

    /**
     * 本地存储路径
     */
    private String localBasePath;

    // COS 配置

    /**
     * COS 密钥
     */
    private String cosSecretId;

    /**
     * COS 密钥
     */
    private String cosSecretKey;

    /**
     * COS 区域
     */
    private String cosRegion;

    /**
     * COS 存储桶
     */
    private String cosBucket;

    /**
     * COS 访问地址前缀
     */
    private String cosUrlPrefix;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMinioEndpoint() {
        return minioEndpoint;
    }

    public void setMinioEndpoint(String minioEndpoint) {
        this.minioEndpoint = minioEndpoint;
    }

    public String getMinioAccessKey() {
        return minioAccessKey;
    }

    public void setMinioAccessKey(String minioAccessKey) {
        this.minioAccessKey = minioAccessKey;
    }

    public String getMinioSecretKey() {
        return minioSecretKey;
    }

    public void setMinioSecretKey(String minioSecretKey) {
        this.minioSecretKey = minioSecretKey;
    }

    public String getMinioBucket() {
        return minioBucket;
    }

    public void setMinioBucket(String minioBucket) {
        this.minioBucket = minioBucket;
    }

    public String getLocalBasePath() {
        return localBasePath;
    }

    public void setLocalBasePath(String localBasePath) {
        this.localBasePath = localBasePath;
    }

    public String getCosSecretId() {
        return cosSecretId;
    }

    public void setCosSecretId(String cosSecretId) {
        this.cosSecretId = cosSecretId;
    }

    public String getCosSecretKey() {
        return cosSecretKey;
    }

    public void setCosSecretKey(String cosSecretKey) {
        this.cosSecretKey = cosSecretKey;
    }

    public String getCosRegion() {
        return cosRegion;
    }

    public void setCosRegion(String cosRegion) {
        this.cosRegion = cosRegion;
    }

    public String getCosBucket() {
        return cosBucket;
    }

    public void setCosBucket(String cosBucket) {
        this.cosBucket = cosBucket;
    }

    public String getCosUrlPrefix() {
        return cosUrlPrefix;
    }

    public void setCosUrlPrefix(String cosUrlPrefix) {
        this.cosUrlPrefix = cosUrlPrefix;
    }
}