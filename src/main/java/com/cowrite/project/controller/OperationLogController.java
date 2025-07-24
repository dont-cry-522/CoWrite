package com.cowrite.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowrite.project.common.ApiResponse;
import com.cowrite.project.common.PageRequest;
import com.cowrite.project.model.entity.OperationLog;
import com.cowrite.project.model.entity.User;
import com.cowrite.project.service.OperationLogService;
import com.cowrite.project.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * OperatorLog 控制器
 * @author Hibiscus-code-generate
 */
@Api(tags = "OperatorLog 控制器")
@RestController
@RequestMapping("/api/operator")
public class OperationLogController {

    private final OperationLogService operationLogService;
    private final UserService userService;

    public OperationLogController(OperationLogService operationLogService, UserService userService) {
        this.operationLogService = operationLogService;
        this.userService = userService;
    }

    @ApiOperation("分页查询操作日志")
    @PostMapping("/page")
    public ApiResponse<Page<OperationLog>> getPage(@RequestBody PageRequest pageRequest) {
        Page<OperationLog> page = new Page<>(pageRequest.getPage(), pageRequest.getSize());
        QueryWrapper<OperationLog> wrapper = new QueryWrapper<>();

        if (pageRequest.getKeyword() != null && !pageRequest.getKeyword().isEmpty()) {
            wrapper.like("description", pageRequest.getKeyword())
                    .or().like("operator", pageRequest.getKeyword());
        }

        if (pageRequest.getSortBy() != null) {
            wrapper.orderBy(true, "asc".equalsIgnoreCase(pageRequest.getSortOrder()), pageRequest.getSortBy());
        } else {
            wrapper.orderByDesc("timestamp");
        }

        return ApiResponse.success(operationLogService.page(page, wrapper));
    }

    @ApiOperation("获取指定操作日志详情")
    @GetMapping("/{id}")
    public ApiResponse<OperationLog> getById(@PathVariable Long id) {
        return ApiResponse.success(operationLogService.getById(id));
    }

    @ApiOperation("获取当前用户的操作日志")
    @GetMapping("/my")
    public ApiResponse<List<OperationLog>> getMyLogs() {
        List<OperationLog> list = operationLogService.list();
        for (OperationLog operationLog : list) {
            System.out.println(operationLog);
        }

        User currentUser = userService.getCurrentUser(); // 需注入 UserService
        LambdaQueryWrapper<OperationLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OperationLog::getUserId, currentUser.getId())
                .orderByDesc(OperationLog::getTimestamp)
                .last("limit 100"); // 限制最多100条
        return ApiResponse.success(operationLogService.list(wrapper));
    }

}
