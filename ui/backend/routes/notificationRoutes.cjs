const express = require('express');
const router = express.Router();

// 获取通知列表（分页 + 类型）
router.get('/', async (req, res) => {
    const { page = 1, size = 10, type } = req.query;
    const token = req.headers['authorization'];
    try {
        const result = await fetchNotifications(page, size, type, token);
        res.status(200).json(result);
    } catch (error) {
        console.error('Error in GET /notification:', error.message);

        // 处理未授权错误
        if (error.response && error.response.status === 401) {
            return res.status(401).json({ message: '未授权：令牌无效或已过期' });
        }

        // 处理其他错误
        res.status(500).json({ message: '获取通知列表时出错' });
    }
});

// 获取未读数量
router.get('/unread-count', async (req, res) => {
    const token = req.headers['authorization'];
    try {
        const result = await fetchUnreadCount(token);
        res.status(200).json(result);
    } catch (error) {
        console.error('Error in GET /notification/unread-count:', error.message);

        // 处理未授权错误
        if (error.response && error.response.status === 401) {
            return res.status(401).json({ message: '未授权：令牌无效或已过期' });
        }

        // 处理其他错误
        res.status(500).json({ message: '获取未读数量时出错' });
    }
});

// 标记单条已读
router.post('/:id/read', async (req, res) => {
    const { id } = req.params;
    const token = req.headers['authorization'];
    try {
        await markNotificationAsRead(id, token);
        res.status(200).json({ message: '标记为已读' });
    } catch (error) {
        console.error(`Error marking notification ${id} as read:`, error.message);

        // 处理未授权错误
        if (error.response && error.response.status === 401) {
            return res.status(401).json({ message: '未授权：令牌无效或已过期' });
        }

        // 处理其他错误
        res.status(500).json({ message: '标记通知为已读时出错' });
    }
});

// 标记全部为已读
router.post('/readAll', async (req, res) => {
    const token = req.headers['authorization'];
    try {
        await markAllNotificationsAsRead(token);
        res.status(200).json({ message: '所有通知标记为已读' });
    } catch (error) {
        console.error('Error marking all as read:', error.message);

        // 处理未授权错误
        if (error.response && error.response.status === 401) {
            return res.status(401).json({ message: '未授权：令牌无效或已过期' });
        }

        // 处理其他错误
        res.status(500).json({ message: '标记所有通知为已读时出错' });
    }
});

// 删除单条通知
router.delete('/:id', async (req, res) => {
    const { id } = req.params;
    const token = req.headers['authorization'];
    try {
        await deleteNotification(id, token);
        res.status(200).json({ message: '通知已删除' });
    } catch (error) {
        console.error(`Error deleting notification ${id}:`, error.message);

        // 处理未授权错误
        if (error.response && error.response.status === 401) {
            return res.status(401).json({ message: '未授权：令牌无效或已过期' });
        }

        // 处理其他错误
        res.status(500).json({ message: '删除通知时出错' });
    }
});

// 清空所有通知
router.delete('/clear', async (req, res) => {
    const token = req.headers['authorization'];
    try {
        await clearAllNotifications(token);
        res.status(200).json({ message: '所有通知已清空' });
    } catch (error) {
        console.error('Error clearing notifications:', error.message);

        // 处理未授权错误
        if (error.response && error.response.status === 401) {
            return res.status(401).json({ message: '未授权：令牌无效或已过期' });
        }

        // 处理其他错误
        res.status(500).json({ message: '清空通知时出错' });
    }
});

module.exports = router;


const axios = require('axios');
const REMOTE_API_BASE_URL = process.env.REMOTE_SERVER || 'http://localhost:8080';

const getHeaders = (token) => ({
    Authorization: token,
    'Content-Type': 'application/json'
});

// 获取通知列表
const fetchNotifications = async (page, size, type, token) => {
    const params = new URLSearchParams({ page, size });
    if (type) params.append('type', type);

    const response = await axios.get(`${REMOTE_API_BASE_URL}/api/notification?${params}`, {
        headers: getHeaders(token)
    });
    return response.data;
};

// 获取未读通知数量
const fetchUnreadCount = async (token) => {
    const response = await axios.get(`${REMOTE_API_BASE_URL}/api/notification/unread-count`, {
        headers: getHeaders(token)
    });
    return response.data;
};

// 标记单条通知为已读
const markNotificationAsRead = async (id, token) => {
    await axios.post(`${REMOTE_API_BASE_URL}/api/notification/${id}/read`, {}, {
        headers: getHeaders(token)
    });
};

// 标记全部通知为已读
const markAllNotificationsAsRead = async (token) => {
    await axios.post(`${REMOTE_API_BASE_URL}/api/notification/readAll`, {}, {
        headers: getHeaders(token)
    });
};

// 删除单条通知
const deleteNotification = async (id, token) => {
    await axios.delete(`${REMOTE_API_BASE_URL}/api/notification/${id}`, {
        headers: getHeaders(token)
    });
};

// 清空所有通知
const clearAllNotifications = async (token) => {
    await axios.delete(`${REMOTE_API_BASE_URL}/api/notification/clear`, {
        headers: getHeaders(token)
    });
};