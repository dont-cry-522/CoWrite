package com.cowrite.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cowrite.project.model.entity.Plugin;
import org.springframework.web.multipart.MultipartFile;

/**
 * Plugin 服务接口
 * @author Hibiscus-code-generate
 */
public interface PluginService extends IService<Plugin> {

    String savePluginFile(MultipartFile file);
}
