package com.cowrite.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowrite.project.common.ApiResponse;
import com.cowrite.project.common.PageRequest;
import com.cowrite.project.model.entity.DocumentFavorite;
import com.cowrite.project.service.DocumentFavoriteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * DocumentFavorite 控制器，提供基础增删改查接口
 * @author Hibiscus-code-generate
 */
@RestController
@RequestMapping("/api/document_favorite")
public class DocumentFavoriteController {

    private final DocumentFavoriteService documentFavoriteService;

    public DocumentFavoriteController(DocumentFavoriteService documentFavoriteService) {
        this.documentFavoriteService = documentFavoriteService;
    }

    /**
     * 新增 DocumentFavorite 记录
     * @param entity 实体对象
     * @return 是否新增成功
     */
    @PostMapping
    public ApiResponse<Boolean> add(@RequestBody DocumentFavorite entity) {
        return ApiResponse.success(documentFavoriteService.save(entity));
    }

    /**
     * 更新 DocumentFavorite 记录
     * @param entity 实体对象（必须包含主键 ID）
     * @return 是否更新成功
     */
    @PutMapping
    public ApiResponse<Boolean> update(@RequestBody DocumentFavorite entity) {
        return ApiResponse.success(documentFavoriteService.updateById(entity));
    }

    /**
     * 删除指定 ID 的 DocumentFavorite 记录
     * @param id 主键 ID
     * @return 是否删除成功
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable("id") Integer id) {
        return ApiResponse.success(documentFavoriteService.removeById(id));
    }

    /**
     * 根据 ID 获取 DocumentFavorite 详情
     * @param id 主键 ID
     * @return 匹配的实体对象
     */
    @GetMapping("/{id}")
    public ApiResponse<DocumentFavorite> getById(@PathVariable("id") Integer id) {
        return ApiResponse.success(documentFavoriteService.getById(id));
    }

    /**
     * 获取所有 DocumentFavorite 列表（不分页）
     * @return 实体列表
     */
    @GetMapping
    public ApiResponse<List<DocumentFavorite>> list() {
        return ApiResponse.success(documentFavoriteService.list());
    }

    /**
     * 分页查询 DocumentFavorite 列表
     * 支持关键字模糊搜索与排序
     * @param pageRequest 分页与筛选请求参数
     * @return 分页结果
     */
    @PostMapping("/page")
    public ApiResponse<Page<DocumentFavorite>> getPage(@RequestBody PageRequest pageRequest) {
        Page<DocumentFavorite> page = new Page<>(pageRequest.getPage(), pageRequest.getSize());
        QueryWrapper<DocumentFavorite> wrapper = new QueryWrapper<>();

        if (pageRequest.getKeyword() != null && !pageRequest.getKeyword().isEmpty()) {
            wrapper.like("name", pageRequest.getKeyword()); // 可自定义字段
        }

        if (pageRequest.getSortBy() != null && !pageRequest.getSortBy().isEmpty()) {
            wrapper.orderBy(true, "asc".equalsIgnoreCase(pageRequest.getSortOrder()), pageRequest.getSortBy());
        }

        return ApiResponse.success(documentFavoriteService.page(page, wrapper));
    }
}
