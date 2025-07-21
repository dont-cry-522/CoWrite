package com.cowrite.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cowrite.project.common.ApiResponse;
import com.cowrite.project.common.context.AuthContext;
import com.cowrite.project.common.enums.NotificationType;
import com.cowrite.project.model.entity.Notification;
import com.cowrite.project.model.entity.User;
import com.cowrite.project.service.NotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * Notification 控制器，提供基础增删改查接口
 * @author Hibiscus-code-generate
 */
@Api(tags = "Notification 控制器")
@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    /**
     * Logger
     */
    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

    /**
     * NotificationService
     */
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * 获取当前用户通知列表（分页 + 类型筛选）
     */
    @GetMapping
    @ApiOperation("获取当前用户的通知列表")
    public ApiResponse<IPage<Notification>> getMyNotifications(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) NotificationType type
    ) {
        User currentUser = AuthContext.getCurrentUser();
        IPage<Notification> result = notificationService.getNotificationsByUserIdPaged(currentUser.getId(), page, size, type);
        return ApiResponse.success(result);
    }

    /**
     * 获取未读通知数量（红点用）
     */
    @GetMapping("/unread-count")
    @ApiOperation("获取未读通知数量")
    public ApiResponse<Long> getUnreadCount() {
        User currentUser = AuthContext.getCurrentUser();
        long count = notificationService.countUnreadByUserId(currentUser.getId());
        return ApiResponse.success(count);
    }

    /**
     * 标记单条通知为已读
     */
    @PostMapping("/{id}/read")
    @ApiOperation("标记单条通知为已读")
    public ApiResponse<Void> markAsRead(@PathVariable Long id) {
        User currentUser = AuthContext.getCurrentUser();
        notificationService.markAsRead(currentUser.getId(), id);
        return ApiResponse.success();
    }

    /**
     * 将所有通知标记为已读
     */
    @PostMapping("/readAll")
    @ApiOperation("将所有通知标记为已读")
    public ApiResponse<Void> markAllAsRead() {
        User currentUser = AuthContext.getCurrentUser();
        notificationService.markAllAsRead(currentUser.getId());
        return ApiResponse.success();
    }

    /**
     * 删除单条通知
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除单条通知")
    public ApiResponse<Void> deleteNotification(@PathVariable Long id) {
        User currentUser = AuthContext.getCurrentUser();
        notificationService.deleteByUserAndId(currentUser.getId(), id);
        return ApiResponse.success();
    }

    /**
     * 清空所有通知
     */
    @DeleteMapping("/clear")
    @ApiOperation("清空所有通知")
    public ApiResponse<Void> clearAllNotifications() {
        User currentUser = AuthContext.getCurrentUser();
        notificationService.clearAllByUserId(currentUser.getId());
        return ApiResponse.success();
    }
}
