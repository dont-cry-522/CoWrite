package com.cowrite.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowrite.project.common.ApiResponse;
import com.cowrite.project.common.PageRequest;
import com.cowrite.project.model.entity.DocumentComment;
import com.cowrite.project.service.DocumentCommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * DocumentComment 控制器，提供基础增删改查接口
 * @author Hibiscus-code-generate
 */
@RestController
@RequestMapping("/api/document_comment")
public class DocumentCommentController {

    private final DocumentCommentService documentCommentService;

    public DocumentCommentController(DocumentCommentService documentCommentService) {
        this.documentCommentService = documentCommentService;
    }

    /**
     * 新增 DocumentComment 记录
     * @param entity 实体对象
     * @return 是否新增成功
     */
    @PostMapping
    public ApiResponse<Boolean> add(@RequestBody DocumentComment entity) {
        return ApiResponse.success(documentCommentService.save(entity));
    }

    /**
     * 更新 DocumentComment 记录
     * @param entity 实体对象（必须包含主键 ID）
     * @return 是否更新成功
     */
    @PutMapping
    public ApiResponse<Boolean> update(@RequestBody DocumentComment entity) {
        return ApiResponse.success(documentCommentService.updateById(entity));
    }

    /**
     * 删除指定 ID 的 DocumentComment 记录
     * @param id 主键 ID
     * @return 是否删除成功
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable("id") Integer id) {
        return ApiResponse.success(documentCommentService.removeById(id));
    }

    /**
     * 根据 ID 获取 DocumentComment 详情
     * @param id 主键 ID
     * @return 匹配的实体对象
     */
    @GetMapping("/{id}")
    public ApiResponse<DocumentComment> getById(@PathVariable("id") Integer id) {
        return ApiResponse.success(documentCommentService.getById(id));
    }

    /**
     * 获取所有 DocumentComment 列表（不分页）
     * @return 实体列表
     */
    @GetMapping
    public ApiResponse<List<DocumentComment>> list() {
        return ApiResponse.success(documentCommentService.list());
    }

    /**
     * 分页查询 DocumentComment 列表
     * 支持关键字模糊搜索与排序
     * @param pageRequest 分页与筛选请求参数
     * @return 分页结果
     */
    @PostMapping("/page")
    public ApiResponse<Page<DocumentComment>> getPage(@RequestBody PageRequest pageRequest) {
        Page<DocumentComment> page = new Page<>(pageRequest.getPage(), pageRequest.getSize());
        QueryWrapper<DocumentComment> wrapper = new QueryWrapper<>();

        if (pageRequest.getKeyword() != null && !pageRequest.getKeyword().isEmpty()) {
            wrapper.like("name", pageRequest.getKeyword()); // 可自定义字段
        }

        if (pageRequest.getSortBy() != null && !pageRequest.getSortBy().isEmpty()) {
            wrapper.orderBy(true, "asc".equalsIgnoreCase(pageRequest.getSortOrder()), pageRequest.getSortBy());
        }

        return ApiResponse.success(documentCommentService.page(page, wrapper));
    }
}
