package com.cowrite.project.common.storage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.cowrite.project.common.constants.Constants.*;

/**
 * 存储服务配置
 */
@Configuration
public class StorageConfig {

    /**
     * Minio存储服务
     */
    private final MinioStorageService minioStorageService;

    /**
     * 本地存储服务
     */
    private final LocalStorageService localStorageService;

    /**
     * 腾讯云存储
     */
    private final CosStorageService cosStorageService;

    /**
     * 文件存储属性
     */
    private final FileStorageProperties properties;

    public StorageConfig(MinioStorageService minioStorageService, LocalStorageService localStorageService, CosStorageService cosStorageService, FileStorageProperties properties) {
        this.minioStorageService = minioStorageService;
        this.localStorageService = localStorageService;
        this.cosStorageService = cosStorageService;
        this.properties = properties;
    }

    /**
     * 获取文件存储服务
     */
    @Bean
    public FileStorageAdapter fileStorageAdapter() {
        return switch (properties.getType()) {
            case STORAGE_TYPE_MINIO -> minioStorageService;
            case STORAGE_TYPE_LOCAL -> localStorageService;
            case STORAGE_TYPE_COS -> cosStorageService;
            default -> throw new IllegalArgumentException("不支持的存储类型: " + properties.getType());
        };
    }
}