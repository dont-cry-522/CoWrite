package com.cowrite.project.common.storage;


import com.cowrite.project.exception.BusinessException;
import io.minio.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

import static com.cowrite.project.common.enums.ResponseCodeEnum.SYSTEM_ERROR;


/**
 * MinIO存储服务
 */
@Service
public class MinioStorageService implements FileStorageAdapter {

    /**
     * MinIO客户端
     */
    private final MinioClient minioClient;

    /**
     * 文件存储属性
     */
    private final FileStorageProperties properties;

    public MinioStorageService(MinioClient minioClient, FileStorageProperties properties) {
        this.minioClient = minioClient;
        this.properties = properties;
    }

    /**
     * 上传文件
     */
    @Override
    public String upload(MultipartFile file) {
        try {
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(properties.getMinioBucket())
                            .object(filename)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return properties.getMinioEndpoint() + '/' + properties.getMinioBucket() + '/' + filename;
        } catch (Exception e) {
            throw new BusinessException(SYSTEM_ERROR.code(), "MinIO上传失败"+ e);
        }
    }

    /**
     * 下载文件
     */
    @Override
    public byte[] download(String path) {
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(properties.getMinioBucket())
                        .object(path)
                        .build())) {
            return stream.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("MinIO下载失败", e);
        }
    }

    /**
     * 删除文件
     */
    @Override
    public void delete(String path) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(properties.getMinioBucket())
                            .object(path)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("MinIO删除失败", e);
        }
    }

    /**
     * 文件是否存在
     */
    @Override
    public boolean exists(String path) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(properties.getMinioBucket())
                            .object(path)
                            .build());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取文件URL
     */
    @Override
    public String getUrl(String path) {
        return properties.getMinioEndpoint() + "/" + properties.getMinioBucket() + "/" + path;
    }

    /**
     * 获取文件大小
     */
    @Override
    public long getFileSize(String path) {
        try {
            return minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(properties.getMinioBucket())
                            .object(path)
                            .build()).size();
        } catch (Exception e) {
            throw new RuntimeException("获取MinIO文件大小失败", e);
        }
    }

    /**
     * 获取文件类型
     */
    @Override
    public String getContentType(String path) {
        try {
            return minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(properties.getMinioBucket())
                            .object(path)
                            .build()).contentType();
        } catch (Exception e) {
            throw new RuntimeException("获取MinIO文件类型失败", e);
        }
    }

    /**
     * 重命名文件
     */
    @Override
    public void rename(String oldPath, String newPath) {
        copy(oldPath, newPath);
        delete(oldPath);
    }

    /**
     * 移动文件
     */
    @Override
    public void copy(String sourcePath, String targetPath) {
        try {
            minioClient.copyObject(
                    CopyObjectArgs.builder()
                            .source(CopySource.builder()
                                    .bucket(properties.getMinioBucket())
                                    .object(sourcePath)
                                    .build())
                            .bucket(properties.getMinioBucket())
                            .object(targetPath)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("MinIO复制失败", e);
        }
    }

    /**
     * 移动文件
     */
    @Override
    public void move(String sourcePath, String targetPath) {
        rename(sourcePath, targetPath);
    }
}