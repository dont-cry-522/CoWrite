package com.cowrite.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowrite.project.common.ApiResponse;
import com.cowrite.project.common.PageRequest;
import com.cowrite.project.model.entity.Tag;
import com.cowrite.project.service.TagService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Tag 控制器，提供基础增删改查接口
 * @author Hibiscus-code-generate
 */
@RestController
@RequestMapping("/api/tag")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * 新增 Tag 记录
     * @param entity 实体对象
     * @return 是否新增成功
     */
    @PostMapping
    public ApiResponse<Boolean> add(@RequestBody Tag entity) {
        if (entity.getName() == null || entity.getName().trim().isEmpty()) {
            return ApiResponse.error("标签名称不能为空");
        }
        if (tagService.count(new QueryWrapper<Tag>().eq("name", entity.getName())) > 0) {
            return ApiResponse.error("标签名称已存在");
        }
        return ApiResponse.success(tagService.save(entity));
    }

    /**
     * 更新 Tag 记录
     * @param entity 实体对象（必须包含主键 ID）
     * @return 是否更新成功
     */
    @PutMapping
    public ApiResponse<Boolean> update(@RequestBody Tag entity) {
        if (entity.getName() == null || entity.getName().trim().isEmpty()) {
            return ApiResponse.error("标签名称不能为空");
        }
        if (tagService.count(new QueryWrapper<Tag>().eq("name", entity.getName())) > 0) {
            return ApiResponse.error("标签名称已存在");
        }
        return ApiResponse.success(tagService.updateById(entity));
    }

    /**
     * 删除指定 ID 的 Tag 记录
     * @param id 主键 ID
     * @return 是否删除成功
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable("id") Integer id) {
        Tag tag = tagService.getById(id);
        if (tag == null){
            return ApiResponse.error("标签不存在");
        }
        return ApiResponse.success(tagService.removeById(id));
    }

    /**
     * 根据 ID 获取 Tag 详情
     * @param id 主键 ID
     * @return 匹配的实体对象
     */
    @GetMapping("/{id}")
    public ApiResponse<Tag> getById(@PathVariable("id") Integer id) {
        Tag tag = tagService.getById(id);
        if (tag == null) {
            return ApiResponse.error("标签不存在");
        }
        return ApiResponse.success(tag);
    }

    /**
     * 获取所有 Tag 列表（不分页）
     * @return 实体列表
     */
    @GetMapping
    public ApiResponse<List<Tag>> list() {
        return ApiResponse.success(tagService.list());
    }

    /**
     * 批量删除选中 Tag
     * @param ids
     * @return
     */
    @DeleteMapping("/batch")
    public ApiResponse<Boolean> batchDelete(@RequestBody List<Integer> ids) {
        if (ids == null){
            return ApiResponse.error("未选择标签");
        }
        return ApiResponse.success(tagService.removeByIds(ids));
    }

    /**
     * 分页查询 Tag 列表
     * 支持关键字模糊搜索与排序
     * @param pageRequest 分页与筛选请求参数
     * @return 分页结果
     */
    @PostMapping("/page")
    public ApiResponse<Page<Tag>> getPage(@RequestBody PageRequest pageRequest) {
        Page<Tag> page = new Page<>(pageRequest.getPage(), pageRequest.getSize());
        QueryWrapper<Tag> wrapper = new QueryWrapper<>();

        if (pageRequest.getKeyword() != null && !pageRequest.getKeyword().isEmpty()) {
            wrapper.like("name", pageRequest.getKeyword()); // 可自定义字段
        }

        if (pageRequest.getSortBy() != null && !pageRequest.getSortBy().isEmpty()) {
            wrapper.orderBy(true, "asc".equalsIgnoreCase(pageRequest.getSortOrder()), pageRequest.getSortBy());
        }

        return ApiResponse.success(tagService.page(page, wrapper));
    }
}
