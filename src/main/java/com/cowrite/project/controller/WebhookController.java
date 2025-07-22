package com.cowrite.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowrite.project.common.ApiResponse;
import com.cowrite.project.common.PageRequest;
import com.cowrite.project.model.entity.Webhook;
import com.cowrite.project.service.WebhookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Webhook 控制器，提供基础增删改查接口
 * @author Hibiscus-code-generate
 */
@RestController
@RequestMapping("/api/webhook")
public class WebhookController {

    private final WebhookService webhookService;

    public WebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    /**
     * 新增 Webhook 记录
     * @param entity 实体对象
     * @return 是否新增成功
     */
    @PostMapping
    public ApiResponse<Boolean> add(@RequestBody Webhook entity) {
        return ApiResponse.success(webhookService.save(entity));
    }

    /**
     * 更新 Webhook 记录
     * @param entity 实体对象（必须包含主键 ID）
     * @return 是否更新成功
     */
    @PutMapping
    public ApiResponse<Boolean> update(@RequestBody Webhook entity) {
        return ApiResponse.success(webhookService.updateById(entity));
    }

    /**
     * 删除指定 ID 的 Webhook 记录
     * @param id 主键 ID
     * @return 是否删除成功
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable("id") Integer id) {
        return ApiResponse.success(webhookService.removeById(id));
    }

    /**
     * 根据 ID 获取 Webhook 详情
     * @param id 主键 ID
     * @return 匹配的实体对象
     */
    @GetMapping("/{id}")
    public ApiResponse<Webhook> getById(@PathVariable("id") Integer id) {
        return ApiResponse.success(webhookService.getById(id));
    }

    /**
     * 获取所有 Webhook 列表（不分页）
     * @return 实体列表
     */
    @GetMapping
    public ApiResponse<List<Webhook>> list() {
        return ApiResponse.success(webhookService.list());
    }

    /**
     * 分页查询 Webhook 列表
     * 支持关键字模糊搜索与排序
     * @param pageRequest 分页与筛选请求参数
     * @return 分页结果
     */
    @PostMapping("/page")
    public ApiResponse<Page<Webhook>> getPage(@RequestBody PageRequest pageRequest) {
        Page<Webhook> page = new Page<>(pageRequest.getPage(), pageRequest.getSize());
        QueryWrapper<Webhook> wrapper = new QueryWrapper<>();

        if (pageRequest.getKeyword() != null && !pageRequest.getKeyword().isEmpty()) {
            wrapper.like("name", pageRequest.getKeyword()); // 可自定义字段
        }

        if (pageRequest.getSortBy() != null && !pageRequest.getSortBy().isEmpty()) {
            wrapper.orderBy(true, "asc".equalsIgnoreCase(pageRequest.getSortOrder()), pageRequest.getSortBy());
        }

        return ApiResponse.success(webhookService.page(page, wrapper));
    }
}
