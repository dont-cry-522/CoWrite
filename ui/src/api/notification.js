import request from '../utils/request';

/**
 * 获取当前用户的通知列表（分页 + 可选类型筛选）
 * @param {Object} params - 包含 page, size, type（可选）
 */
export const getNotifications = (params = {}) => {
    return request.get('/notifications', { params }); // 对应 Node 路由 /api/notifications
};

/**
 * 获取当前用户的未读通知数量
 */
export const getUnreadCount = () => {
    return request.get('/notifications/unread-count');
};

/**
 * 标记单条通知为已读
 * @param {number|string} id - 通知 ID
 */
export const markAsRead = (id) => {
    return request.post(`/notifications/${id}/read`);
};

/**
 * 标记所有通知为已读
 */
export const markAllAsRead = () => {
    return request.post('/notifications/readAll');
};

/**
 * 删除某条通知
 * @param {number|string} id - 通知 ID
 */
export const deleteNotification = (id) => {
    return request.delete(`/notifications/${id}`);
};

/**
 * 清空所有通知
 */
export const clearAllNotifications = () => {
    return request.delete('/notifications/clear');
};
