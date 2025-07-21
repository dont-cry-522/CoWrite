package com.cowrite.project.common.storage;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.CannedAccessControlList;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * 腾讯云存储服务
 */
@Service
public class CosStorageService implements FileStorageAdapter {

    /**
     * 存储属性类
     */
    private final FileStorageProperties properties;

    public CosStorageService(FileStorageProperties properties) {
        this.properties = properties;
    }

    /**
     * 创建 COS 客户端
     */
    private COSClient buildClient() {
        COSCredentials credentials = new BasicCOSCredentials(
                properties.getCosSecretId(), properties.getCosSecretKey());
        ClientConfig clientConfig = new ClientConfig(new Region(properties.getCosRegion()));
        return new COSClient(credentials, clientConfig);
    }

    /**
     * 上传文件
     */
    @Override
    public String upload(MultipartFile file) {
        COSClient cosClient = null;
        try {
            cosClient = buildClient();
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            PutObjectRequest request = new PutObjectRequest(
                    properties.getCosBucket(), filename, file.getInputStream(), metadata);
            request.setCannedAcl(CannedAccessControlList.PublicRead);

            cosClient.putObject(request);

            return filename; // 或拼接返回完整 URL

        } catch (Exception e) {
            throw new RuntimeException("COS 上传失败", e);
        } finally {
            if (cosClient != null) {
                cosClient.shutdown();
            }
        }
    }

    /**
     * 下载文件
     */
    @Override
    public byte[] download(String path) {
        COSClient cosClient = null;
        try {
            cosClient = buildClient();

            try (InputStream inputStream = cosClient.getObject(properties.getCosBucket(), path).getObjectContent();
                 ByteArrayOutputStream output = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[8192];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, len);
                }
                return output.toByteArray();
            }

        } catch (Exception e) {
            throw new RuntimeException("COS 下载失败", e);
        } finally {
            if (cosClient != null) {
                cosClient.shutdown();
            }
        }
    }

    /**
     * 删除文件
     */
    @Override
    public void delete(String path) {
        COSClient cosClient = null;
        try {
            cosClient = buildClient();
            cosClient.deleteObject(properties.getCosBucket(), path);
        } catch (Exception e) {
            throw new RuntimeException("COS 删除失败", e);
        } finally {
            if (cosClient != null) {
                cosClient.shutdown();
            }
        }
    }

    /**
     * 判断文件是否存在
     */
    @Override
    public boolean exists(String path) {
        COSClient cosClient = null;
        try {
            cosClient = buildClient();
            return cosClient.doesObjectExist(properties.getCosBucket(), path);
        } catch (Exception e) {
            throw new RuntimeException("COS 检测文件是否存在失败", e);
        }
    }

    /**
     * 获取文件 URL
     */
    @Override
    public String getUrl(String path) {
        return properties.getCosUrlPrefix() + "/" + path;
    }

    /**
     * 获取文件大小
     */
    @Override
    public long getFileSize(String path) {
        try {
            COSClient client = buildClient();
            return client.getObjectMetadata(properties.getCosBucket(), path).getContentLength();
        } catch (Exception e) {
            throw new RuntimeException("COS 获取文件大小失败", e);
        }
    }

    /**
     * 获取文件类型
     */
    @Override
    public String getContentType(String path) {
        try {
            COSClient client = buildClient();
            return client.getObjectMetadata(properties.getCosBucket(), path).getContentType();
        } catch (Exception e) {
            throw new RuntimeException("COS 获取文件类型失败", e);
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
     * 文件复制
     */
    @Override
    public void copy(String sourcePath, String targetPath) {
        try {
            COSClient client = buildClient();
            client.copyObject(properties.getCosBucket(), sourcePath,
                    properties.getCosBucket(), targetPath);
        } catch (Exception e) {
            throw new RuntimeException("COS 文件复制失败", e);
        }
    }

    /**
     * 文件移动
     */
    @Override
    public void move(String sourcePath, String targetPath) {
        rename(sourcePath, targetPath);
    }
}