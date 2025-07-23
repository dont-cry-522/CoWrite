package com.cowrite.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowrite.project.common.ApiResponse;
import com.cowrite.project.common.PageRequest;
import com.cowrite.project.model.entity.OrganizationInvite;
import com.cowrite.project.service.OrganizationInviteService;
import com.j256.simplemagic.logger.Logger;
import com.j256.simplemagic.logger.LoggerFactory;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Organization Invite 控制器，提供基础增删改查接口
 * 该控制器处理组织邀请管理相关的所有 API 请求。
 * @author Hibiscus-code-generate
 */
@Api(tags = "Organization Invite 控制器")
@RestController
@RequestMapping("/api/organization/invite")
public class OrganizationInviteController {

    /**
     * 日志记录器
     * 用于记录组织邀请控制器中的操作日志
     */
    private static final Logger log = LoggerFactory.getLogger(OrganizationInvite.class);

    /**
     * 组织邀请服务
     * 负责处理组织邀请相关的业务逻辑
     */
    private final OrganizationInviteService organizationInviteService;

    public OrganizationInviteController(OrganizationInviteService organizationInviteService) {
        this.organizationInviteService = organizationInviteService;
    }

    /**
     * 新增 OrganizationInvite 记录
     * 用于新增组织邀请记录
     * @param entity 新的组织邀请实体对象
     * @return 返回是否新增成功的响应
     */
    @PostMapping
    public ApiResponse<Boolean> add(@RequestBody OrganizationInvite entity) {
        return ApiResponse.success(organizationInviteService.save(entity));
    }

    /**
     * 更新 OrganizationInvite 记录
     * 用于更新现有的组织邀请记录，必须提供包含 ID 的组织邀请实体。
     * @param entity 更新后的组织邀请实体对象（必须包含 ID）
     * @return 返回是否更新成功的响应
     */
    @PutMapping("/{id}")
    public ApiResponse<Boolean> update(@RequestBody OrganizationInvite entity) {
        return ApiResponse.success(organizationInviteService.updateById(entity));
    }

    /**
     * 删除指定 ID 的 OrganizationInvite 记录
     * 根据提供的 ID 删除组织邀请记录。
     * @param id 要删除的组织邀请记录的主键 ID
     * @return 返回是否删除成功的响应
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable("id") Integer id) {
        return ApiResponse.success(organizationInviteService.removeById(id));
    }

    /**
     * 根据 ID 获取 OrganizationInvite 详情
     * 根据提供的 ID 查询组织邀请记录的详细信息。
     * @param id 要查询的组织邀请记录的主键 ID
     * @return 返回查询到的组织邀请实体对象
     */
    @GetMapping("/{id}")
    public ApiResponse<OrganizationInvite> getById(@PathVariable("id") Integer id) {
        return ApiResponse.success(organizationInviteService.getById(id));
    }

    /**
     * 获取所有 OrganizationInvite 列表（不分页）
     * 返回所有的组织邀请记录，不进行分页。
     * @return 返回所有组织邀请的实体列表
     */
    @GetMapping
    public ApiResponse<List<OrganizationInvite>> list() {
        return ApiResponse.success(organizationInviteService.list());
    }

    /**
     * 分页查询 OrganizationInvite 列表
     * 提供分页查询组织邀请记录的功能，支持关键字搜索和排序。
     * @param pageRequest 分页请求对象，包含页码、每页大小、搜索关键词、排序字段等
     * @return 返回分页后的组织邀请列表
     */
    @PostMapping("/page")
    public ApiResponse<Page<OrganizationInvite>> getPage(@RequestBody PageRequest pageRequest) {
        // 创建分页对象
        Page<OrganizationInvite> page = new Page<>(pageRequest.getPage(), pageRequest.getSize());

        // 构造查询条件
        QueryWrapper<OrganizationInvite> wrapper = new QueryWrapper<>();

        // 如果有关键字，进行模糊查询
        if (pageRequest.getKeyword() != null && !pageRequest.getKeyword().isEmpty()) {
            wrapper.like("name", pageRequest.getKeyword()); // 可自定义字段（如“name”）
        }

        // 如果有排序字段，进行排序
        if (pageRequest.getSortBy() != null && !pageRequest.getSortBy().isEmpty()) {
            wrapper.orderBy(true, "asc".equalsIgnoreCase(pageRequest.getSortOrder()), pageRequest.getSortBy());
        }

        // 返回分页结果
        return ApiResponse.success(organizationInviteService.page(page, wrapper));
    }
}
