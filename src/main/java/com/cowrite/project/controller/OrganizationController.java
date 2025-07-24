package com.cowrite.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowrite.project.common.ApiResponse;
import com.cowrite.project.common.PageRequest;
import com.cowrite.project.model.entity.Organization;
import com.cowrite.project.service.OrganizationService;
import com.j256.simplemagic.logger.Logger;
import com.j256.simplemagic.logger.LoggerFactory;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Organization 控制器，提供基础增删改查接口
 * 该控制器处理组织管理相关的所有 API 请求。
 * @author Hibiscus-code-generate
 */
@Api(tags = "Organization 控制器")
@RestController
@RequestMapping("/api/organization")
public class OrganizationController {

    /**
     * 日志记录器
     * 用于记录组织管理控制器中的操作日志
     */
    private static final Logger log = LoggerFactory.getLogger(Organization.class);

    /**
     * 组织服务
     * 负责处理组织相关的业务逻辑
     */
    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    /**
     * 新增 Organization 记录
     */
    @PostMapping
    public ApiResponse<Boolean> add(@RequestBody Organization entity) {
        return ApiResponse.success(organizationService.save(entity));
    }

    /**
     * 更新 Organization 记录
     */
    @PutMapping("/{id}")
    public ApiResponse<Boolean> update(@RequestBody Organization entity) {
        return ApiResponse.success(organizationService.updateById(entity));
    }

    /**
     * 删除指定 ID 的 Organization 记录
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable("id") Integer id) {
        return ApiResponse.success(organizationService.removeById(id));
    }

    /**
     * 根据 ID 获取 Organization 详情
     */
    @GetMapping("/{id}")
    public ApiResponse<Organization> get(@PathVariable("id") Integer id) {
        return ApiResponse.success(organizationService.getById(id));
    }

    /**
     * 获取所有 Organization 列表（不分页）
     */
    @GetMapping
    public ApiResponse<List<Organization>> list() {
        return ApiResponse.success(organizationService.list());
    }

    /**
     * 分页查询 Organization 列表
     */
    @PostMapping("/page")
    public ApiResponse<Page<Organization>> getPage(@RequestBody PageRequest pageRequest) {
        // 创建分页对象
        Page<Organization> page = new Page<>(pageRequest.getPage(), pageRequest.getSize());

        // 构造查询条件
        QueryWrapper<Organization> wrapper = new QueryWrapper<>();

        // 如果有关键字，进行模糊查询
        if (pageRequest.getKeyword() != null && !pageRequest.getKeyword().isEmpty()) {
            wrapper.like("name", pageRequest.getKeyword()); // 可以自定义字段（如“name”）
        }

        // 如果有排序字段，进行排序
        if (pageRequest.getSortBy() != null && !pageRequest.getSortBy().isEmpty()) {
            wrapper.orderBy(true, "asc".equalsIgnoreCase(pageRequest.getSortOrder()), pageRequest.getSortBy());
        }

        // 返回分页结果
        return ApiResponse.success(organizationService.page(page, wrapper));
    }
}
