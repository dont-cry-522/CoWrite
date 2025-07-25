package com.cowrite.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowrite.project.common.ApiResponse;
import com.cowrite.project.common.PageRequest;
import com.cowrite.project.model.entity.Plugin;
import com.cowrite.project.service.PluginService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Plugin 控制器，提供基础增删改查接口
 * @author Hibiscus-code-generate
 */
@RestController
@RequestMapping("/api/plugin")
public class PluginController {

    private final PluginService pluginService;

    public PluginController(PluginService pluginService) {
        this.pluginService = pluginService;
    }


    /**
     * 上传插件文件
     * @param file 插件文件
     * @return 上传结果
     */
    @PostMapping("/upload")
    public ApiResponse<String> uploadPlugin(@RequestParam("file") MultipartFile file) {
        String filePath = pluginService.savePluginFile(file);
        return ApiResponse.success("插件上传成功，文件路径: " + filePath);
    }

    /**
     * 新增 Plugin 记录
     * @param entity 实体对象
     * @return 是否新增成功
     */
    @PostMapping
    public ApiResponse<Boolean> add(@RequestBody Plugin entity) {
        return ApiResponse.success(pluginService.save(entity));
    }

    /**
     * 更新 Plugin 记录
     * @param entity 实体对象（必须包含主键 ID）
     * @return 是否更新成功
     */
    @PutMapping
    public ApiResponse<Boolean> update(@RequestBody Plugin entity) {
        return ApiResponse.success(pluginService.updateById(entity));
    }

    /**
     * 删除指定 ID 的 Plugin 记录
     * @param id 主键 ID
     * @return 是否删除成功
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable("id") Integer id) {
        return ApiResponse.success(pluginService.removeById(id));
    }

    /**
     * 根据 ID 获取 Plugin 详情
     * @param id 主键 ID
     * @return 匹配的实体对象
     */
    @GetMapping("/{id}")
    public ApiResponse<Plugin> getById(@PathVariable("id") Integer id) {
        return ApiResponse.success(pluginService.getById(id));
    }

    /**
     * 获取所有 Plugin 列表（不分页）
     * @return 实体列表
     */
    @GetMapping
    public ApiResponse<List<Plugin>> list() {
        return ApiResponse.success(pluginService.list());
    }

    /**
     * 分页查询 Plugin 列表
     * 支持关键字模糊搜索与排序
     * @param pageRequest 分页与筛选请求参数
     * @return 分页结果
     */
    @PostMapping("/page")
    public ApiResponse<Page<Plugin>> getPage(@RequestBody PageRequest pageRequest) {
        Page<Plugin> page = new Page<>(pageRequest.getPage(), pageRequest.getSize());
        QueryWrapper<Plugin> wrapper = new QueryWrapper<>();

        if (pageRequest.getKeyword() != null && !pageRequest.getKeyword().isEmpty()) {
            wrapper.like("name", pageRequest.getKeyword()); // 可自定义字段
        }

        if (pageRequest.getSortBy() != null && !pageRequest.getSortBy().isEmpty()) {
            wrapper.orderBy(true, "asc".equalsIgnoreCase(pageRequest.getSortOrder()), pageRequest.getSortBy());
        }

        return ApiResponse.success(pluginService.page(page, wrapper));
    }
}
