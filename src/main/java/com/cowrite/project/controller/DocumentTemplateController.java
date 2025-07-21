package com.cowrite.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowrite.project.common.ApiResponse;
import com.cowrite.project.common.context.AuthContext;
import com.cowrite.project.model.entity.DocumentTemplate;
import com.cowrite.project.service.DocumentTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * DocumentTemplate 控制器，提供基础增删改查接口
 * @author Hibiscus-code-generate
 */
@Api(tags = "DocumentTemplate 控制器")
@RestController
@RequestMapping("/api/document_template")
public class DocumentTemplateController {

    private final DocumentTemplateService documentTemplateService;

    public DocumentTemplateController(DocumentTemplateService documentTemplateService) {
        this.documentTemplateService = documentTemplateService;
    }

    /**
     * 分页查询文档模板列表
     */
    @ApiOperation("分页查询文档模板列表")
    @GetMapping("/list")
    public ApiResponse<Page<DocumentTemplate>> listTemplates(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String scope,
            @RequestParam(required = false) String title
    ) {
        QueryWrapper<DocumentTemplate> query = new QueryWrapper<>();
        if (scope != null) {
            query.eq("scope", scope);
        }
        if (title != null && !title.isEmpty()) {
            query.like("title", title);
        }
        query.orderByDesc("id");
        Page<DocumentTemplate> pageResult = documentTemplateService.page(new Page<>(page, size), query);
        return ApiResponse.success(pageResult);
    }

    /**
     * 根据ID获取模板详情
     */
    @ApiOperation("根据ID获取模板详情")
    @GetMapping("/{id}")
    public ApiResponse<DocumentTemplate> getTemplate(@PathVariable Long id) {
        DocumentTemplate template = documentTemplateService.getById(id);
        if (template == null) {
            return ApiResponse.error("模板不存在");
        }
        return ApiResponse.success(template);
    }

    /**
     * 新建模板
     */
    @ApiOperation("新建模板")
    @PostMapping("/create")
    public ApiResponse<DocumentTemplate> createTemplate(@RequestBody DocumentTemplate template) {
        template.setCreatorId(getCurrentUserId());
        boolean saved = documentTemplateService.save(template);
        if (!saved) {
            return ApiResponse.error("创建模板失败");
        }
        return ApiResponse.success(template);
    }

    /**
     * 更新模板
     */
    @ApiOperation("更新模板")
    @PostMapping("/update")
    public ApiResponse<DocumentTemplate> updateTemplate(@RequestBody DocumentTemplate template) {
        if (template.getId() == null) {
            return ApiResponse.error("模板ID不能为空");
        }
        boolean updated = documentTemplateService.updateById(template);
        if (!updated) {
            return ApiResponse.error("更新模板失败");
        }
        return ApiResponse.success(template);
    }

    /**
     * 删除模板（逻辑删除）
     */
    @ApiOperation("删除模板（逻辑删除）")
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteTemplate(@PathVariable Long id) {
        DocumentTemplate template = documentTemplateService.getById(id);
        if (template == null) {
            return ApiResponse.error("模板不存在");
        }
        // 这里假设模板实体有 deleted 字段，或者你也可以直接 removeById
        template.setDeleted(true);
        boolean updated = documentTemplateService.updateById(template);
        if (!updated) {
            return ApiResponse.error("删除模板失败");
        }
        return ApiResponse.success();
    }

    /**
     * 获取当前登录用户ID
     */
    private Long getCurrentUserId() {
        return AuthContext.getCurrentUser().getId();
    }
}
