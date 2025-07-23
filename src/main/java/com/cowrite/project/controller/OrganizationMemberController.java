package com.cowrite.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowrite.project.common.ApiResponse;
import com.cowrite.project.common.PageRequest;
import com.cowrite.project.model.entity.OrganizationMember;
import com.cowrite.project.service.OrganizationMemberService;
import com.j256.simplemagic.logger.Logger;
import com.j256.simplemagic.logger.LoggerFactory;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Organization Member 控制器，提供基础增删改查接口
 * 该控制器处理组织成员管理相关的所有 API 请求。
 * @author Hibiscus-code-generate
 */
@Api(tags = "Organization Member 控制器")
@RestController
@RequestMapping("/api/organization/member")
public class OrganizationMemberController {

    /**
     * 日志记录器
     * 用于记录组织成员控制器中的操作日志
     */
    private static final Logger log = LoggerFactory.getLogger(OrganizationMemberController.class);

    /**
     * 组织成员服务
     * 负责处理组织成员相关的业务逻辑
     */
    private final OrganizationMemberService organizationMemberService;

    public OrganizationMemberController(OrganizationMemberService organizationMemberService) {
        this.organizationMemberService = organizationMemberService;
    }

    /**
     * 新增 OrganizationMember 记录
     * 用于新增组织成员记录。
     * @param entity 新的组织成员实体对象
     * @return 返回是否新增成功的响应
     */
    @PostMapping
    public ApiResponse<Boolean> add(@RequestBody OrganizationMember entity) {
        return ApiResponse.success(organizationMemberService.save(entity));
    }

    /**
     * 更新 OrganizationMember 记录
     * 用于更新现有的组织成员记录，必须提供包含 ID 的组织成员实体。
     * @param entity 更新后的组织成员实体对象（必须包含 ID）
     * @return 返回是否更新成功的响应
     */
    @PutMapping("/{id}")
    public ApiResponse<Boolean> update(@RequestBody OrganizationMember entity) {
        return ApiResponse.success(organizationMemberService.updateById(entity));
    }

    /**
     * 删除指定 ID 的 OrganizationMember 记录
     * 根据提供的 ID 删除组织成员记录。
     * @param id 要删除的组织成员记录的主键 ID
     * @return 返回是否删除成功的响应
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable("id") Integer id) {
        return ApiResponse.success(organizationMemberService.removeById(id));
    }

    /**
     * 根据 ID 获取 OrganizationMember 详情
     * 根据提供的 ID 查询组织成员记录的详细信息。
     * @param id 要查询的组织成员记录的主键 ID
     * @return 返回查询到的组织成员实体对象
     */
    @GetMapping("/{id}")
    public ApiResponse<OrganizationMember> getById(@PathVariable("id") Integer id) {
        return ApiResponse.success(organizationMemberService.getById(id));
    }

    /**
     * 获取所有 OrganizationMember 列表（不分页）
     * 返回所有的组织成员记录，不进行分页。
     * @return 返回所有组织成员的实体列表
     */
    @GetMapping
    public ApiResponse<List<OrganizationMember>> list() {
        return ApiResponse.success(organizationMemberService.list());
    }

    /**
     * 分页查询 OrganizationMember 列表
     * 提供分页查询组织成员记录的功能，支持关键字搜索和排序。
     * @param pageRequest 分页请求对象，包含页码、每页大小、搜索关键词、排序字段等
     * @return 返回分页后的组织成员列表
     */
    @PostMapping("/page")
    public ApiResponse<Page<OrganizationMember>> getPage(@RequestBody PageRequest pageRequest) {
        // 创建分页对象
        Page<OrganizationMember> page = new Page<>(pageRequest.getPage(), pageRequest.getSize());

        // 构造查询条件
        QueryWrapper<OrganizationMember> wrapper = new QueryWrapper<>();

        // 如果有关键字，进行模糊查询
        if (pageRequest.getKeyword() != null && !pageRequest.getKeyword().isEmpty()) {
            wrapper.like("name", pageRequest.getKeyword()); // 可自定义字段（如“name”）
        }

        // 如果有排序字段，进行排序
        if (pageRequest.getSortBy() != null && !pageRequest.getSortBy().isEmpty()) {
            wrapper.orderBy(true, "asc".equalsIgnoreCase(pageRequest.getSortOrder()), pageRequest.getSortBy());
        }

        // 返回分页结果
        return ApiResponse.success(organizationMemberService.page(page, wrapper));
    }

}
