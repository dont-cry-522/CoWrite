package com.cowrite.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowrite.project.common.ApiResponse;
import com.cowrite.project.common.PageRequest;
import com.cowrite.project.model.entity.KnowledgeBase;
import com.cowrite.project.service.KnowledgeBaseService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * KnowledgeBase 控制器，提供基础增删改查接口
 * 该控制器处理知识库相关的所有 API 请求。
 * @author Hibiscus-code-generate
 */
@Api(tags = "Knowledge Base 控制器")
@RestController
@RequestMapping("/api/knowledge-base")
public class KnowledgeBaseController {

    /**
     * Logger
     */

    private static final Logger log = LoggerFactory.getLogger(KnowledgeBaseController.class);

    /**
     * KnowledgeBase Service
     */

    private final KnowledgeBaseService knowledgeBaseService;

    public KnowledgeBaseController(KnowledgeBaseService knowledgeBaseService) {
        this.knowledgeBaseService = knowledgeBaseService;
    }

    /**
     * 新增 KnowledgeBase 记录
     * 用于新增知识库记录
     * @param entity 新的知识库实体对象
     * @return 返回是否新增成功的响应
     */
    @PostMapping
    public ApiResponse<Boolean> add(@RequestBody KnowledgeBase entity) {
        return ApiResponse.success(knowledgeBaseService.save(entity));
    }

    /**
     * 更新 KnowledgeBase 记录
     * 用于更新现有的知识库记录
     * @param entity 更新后的知识库实体对象（必须包含 ID）
     * @return 返回是否更新成功的响应
     */
    @PutMapping("/{id}")
    public ApiResponse<Boolean> update(@RequestBody KnowledgeBase entity) {
        return ApiResponse.success(knowledgeBaseService.updateById(entity));
    }

    /**
     * 删除指定 ID 的 KnowledgeBase 记录
     * 根据提供的 ID 删除知识库记录
     * @param id 要删除的知识库记录的主键 ID
     * @return 返回是否删除成功的响应
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable("id") Integer id) {
        return ApiResponse.success(knowledgeBaseService.removeById(id));
    }

    /**
     * 根据 ID 获取 KnowledgeBase 详情
     * 根据提供的 ID 查询知识库记录的详细信息
     * @param id 要查询的知识库记录的主键 ID
     * @return 返回查询到的知识库实体对象
     */
    @GetMapping("/{id}")
    public ApiResponse<KnowledgeBase> getById(@PathVariable("id") Integer id) {
        return ApiResponse.success(knowledgeBaseService.getById(id));
    }

    /**
     * 获取所有 KnowledgeBase 列表（不分页）
     * 返回所有的知识库记录，不进行分页
     * @return 返回所有知识库的实体列表
     */
    @GetMapping
    public ApiResponse<List<KnowledgeBase>> list() {
        return ApiResponse.success(knowledgeBaseService.list());
    }

    /**
     * 分页查询 KnowledgeBase 列表
     * 提供分页查询知识库记录的功能，支持关键字搜索和排序
     * @param pageRequest 分页请求对象，包含页码、每页大小、搜索关键词、排序字段等
     * @return 返回分页后的知识库列表
     */
    @PostMapping("/page")
    public ApiResponse<Page<KnowledgeBase>> getPage(@RequestBody PageRequest pageRequest) {
        // 创建分页对象
        Page<KnowledgeBase> page = new Page<>(pageRequest.getPage(), pageRequest.getSize());

        // 构造查询条件
        QueryWrapper<KnowledgeBase> wrapper = new QueryWrapper<>();

        // 如果有关键字，进行模糊查询
        if (pageRequest.getKeyword() != null && !pageRequest.getKeyword().isEmpty()) {
            wrapper.like("name", pageRequest.getKeyword()); // 可自定义字段（如“name”）
        }

        // 如果有排序字段，进行排序
        if (pageRequest.getSortBy() != null && !pageRequest.getSortBy().isEmpty()) {
            wrapper.orderBy(true, "asc".equalsIgnoreCase(pageRequest.getSortOrder()), pageRequest.getSortBy());
        }

        // 返回分页结果
        return ApiResponse.success(knowledgeBaseService.page(page, wrapper));
    }
}