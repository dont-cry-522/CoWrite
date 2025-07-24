package com.cowrite.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowrite.project.common.enums.NotificationType;
import com.cowrite.project.mapper.NotificationMapper;
import com.cowrite.project.model.entity.Notification;
import com.cowrite.project.service.NotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息通知实现类
 *
 * @author heathcetide
 */
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

    private final NotificationMapper notificationMapper;

    public NotificationServiceImpl(NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
    }

    /**
     * 发送通知
     */
    @Override
    public void sendNotification(Notification notification) {
        notificationMapper.insert(notification);
    }

    /**
     * 获取当前用户通知列表
     */
    @Override
    public IPage<Notification> getNotificationsByUserIdPaged(Long userId, int page, int size, NotificationType type) {
        QueryWrapper<Notification> query = new QueryWrapper<Notification>()
                .eq("user_id", userId)
                .orderByDesc("send_time");
        if (type != null) {
            query.eq("type", type);
        }
        return page(new Page<>(page, size), query);
    }

    /**
     * 获取当前用户未读通知数量
     */
    @Override
    public long countUnreadByUserId(Long userId) {
        return count(new QueryWrapper<Notification>()
                .eq("user_id", userId)
                .eq("is_read", false)
        );
    }

    /**
     * 标记单条通知为已读
     */
    @Override
    public void markAsRead(Long userId, Long notificationId) {
        Notification notification = getOne(new QueryWrapper<Notification>()
                .eq("id", notificationId)
                .eq("user_id", userId)
        );

        if (notification != null && !notification.isRead()) {
            notification.setRead(true);
            notification.setReadTime(LocalDateTime.now());
            updateById(notification);
        }
    }

    /**
     * 将所有通知标记为已读
     */
    @Override
    public void markAllAsRead(Long userId) {
        List<Notification> unreadList = list(new QueryWrapper<Notification>()
                .eq("user_id", userId)
                .eq("is_read", false)
        );

        for (Notification notification : unreadList) {
            notification.setRead(true);
            notification.setReadTime(LocalDateTime.now());
        }
        updateBatchById(unreadList);
    }

    /**
     * 删除单条通知
     */
    @Override
    public void deleteByUserAndId(Long userId, Long notificationId) {
        remove(new QueryWrapper<Notification>()
                .eq("id", notificationId)
                .eq("user_id", userId)
        );
    }

    /**
     * 清空所有通知
     */
    @Override
    public void clearAllByUserId(Long userId) {
        remove(new QueryWrapper<Notification>()
                .eq("user_id", userId)
        );
    }
}
