package com.cowrite.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowrite.project.common.ApiResponse;
import com.cowrite.project.common.PageRequest;
import com.cowrite.project.model.entity.KnowledgeBaseCollaborator;
import com.cowrite.project.service.KnowledgeBaseCollaboratorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * KnowledgeBaseCollaborator 控制器，提供基础增删改查接口
 * @author Hibiscus-code-generate
 */
@RestController
@RequestMapping("/api/knowledge_base_collaborator")
public class KnowledgeBaseCollaboratorController {

    private final KnowledgeBaseCollaboratorService knowledgeBaseCollaboratorService;

    public KnowledgeBaseCollaboratorController(KnowledgeBaseCollaboratorService knowledgeBaseCollaboratorService) {
        this.knowledgeBaseCollaboratorService = knowledgeBaseCollaboratorService;
    }

    /**
     * 新增 KnowledgeBaseCollaborator 记录
     * @param entity 实体对象
     * @return 是否新增成功
     */
    @PostMapping
    public ApiResponse<Boolean> add(@RequestBody KnowledgeBaseCollaborator entity) {
        return ApiResponse.success(knowledgeBaseCollaboratorService.save(entity));
    }

    /**
     * 更新 KnowledgeBaseCollaborator 记录
     * @param entity 实体对象（必须包含主键 ID）
     * @return 是否更新成功
     */
    @PutMapping
    public ApiResponse<Boolean> update(@RequestBody KnowledgeBaseCollaborator entity) {
        return ApiResponse.success(knowledgeBaseCollaboratorService.updateById(entity));
    }

    /**
     * 删除指定 ID 的 KnowledgeBaseCollaborator 记录
     * @param id 主键 ID
     * @return 是否删除成功
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable("id") Integer id) {
        return ApiResponse.success(knowledgeBaseCollaboratorService.removeById(id));
    }

    /**
     * 根据 ID 获取 KnowledgeBaseCollaborator 详情
     * @param id 主键 ID
     * @return 匹配的实体对象
     */
    @GetMapping("/{id}")
    public ApiResponse<KnowledgeBaseCollaborator> getById(@PathVariable("id") Integer id) {
        return ApiResponse.success(knowledgeBaseCollaboratorService.getById(id));
    }

    /**
     * 获取所有 KnowledgeBaseCollaborator 列表（不分页）
     * @return 实体列表
     */
    @GetMapping
    public ApiResponse<List<KnowledgeBaseCollaborator>> list() {
        return ApiResponse.success(knowledgeBaseCollaboratorService.list());
    }

    /**
     * 分页查询 KnowledgeBaseCollaborator 列表
     * 支持关键字模糊搜索与排序
     * @param pageRequest 分页与筛选请求参数
     * @return 分页结果
     */
    @PostMapping("/page")
    public ApiResponse<Page<KnowledgeBaseCollaborator>> getPage(@RequestBody PageRequest pageRequest) {
        Page<KnowledgeBaseCollaborator> page = new Page<>(pageRequest.getPage(), pageRequest.getSize());
        QueryWrapper<KnowledgeBaseCollaborator> wrapper = new QueryWrapper<>();

        if (pageRequest.getKeyword() != null && !pageRequest.getKeyword().isEmpty()) {
            wrapper.like("name", pageRequest.getKeyword()); // 可自定义字段
        }

        if (pageRequest.getSortBy() != null && !pageRequest.getSortBy().isEmpty()) {
            wrapper.orderBy(true, "asc".equalsIgnoreCase(pageRequest.getSortOrder()), pageRequest.getSortBy());
        }

        return ApiResponse.success(knowledgeBaseCollaboratorService.page(page, wrapper));
    }
}
