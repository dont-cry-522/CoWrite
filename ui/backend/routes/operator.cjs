const express = require('express');
const router = express.Router();

// 获取分页操作日志
router.post('/page', async (req, res) => {
    const token = req.headers['authorization'];
    try {
        const result = await fetchOperationLogs(req.body, token);
        res.status(200).json(result);
    } catch (error) {
        console.error('Error fetching logs (page):', error.message);

        // If the error is a 401 Unauthorized, respond with 401 status code
        if (error.response && error.response.status === 401) {
            return res.status(401).json({ message: 'Unauthorized: Invalid or expired token' });
        }

        // For other errors, return 500
        res.status(500).json({ message: 'Error fetching operation logs' });
    }
});

// 获取当前用户的操作日志（最多100条）
router.get('/my', async (req, res) => {
    const token = req.headers['authorization'];
    try {
        const result = await fetchMyLogs(token);
        res.status(200).json(result);
    } catch (error) {
        console.error('Error fetching my logs:', error.message);

        // If the error is a 401 Unauthorized, respond with 401 status code
        if (error.response && error.response.status === 401) {
            return res.status(401).json({ message: 'Unauthorized: Invalid or expired token' });
        }

        // For other errors, return 500
        res.status(500).json({ message: 'Error fetching your logs' });
    }
});

// 获取日志详情
router.get('/:id', async (req, res) => {
    const token = req.headers['authorization'];
    const { id } = req.params;
    try {
        const result = await fetchOperationLogById(id, token);
        res.status(200).json(result);
    } catch (error) {
        console.error(`Error fetching log ${id}:`, error.message);

        // If the error is a 401 Unauthorized, respond with 401 status code
        if (error.response && error.response.status === 401) {
            return res.status(401).json({ message: 'Unauthorized: Invalid or expired token' });
        }

        // For other errors, return 500
        res.status(500).json({ message: 'Error fetching log details' });
    }
});

module.exports = router;

const axios = require('axios');
const REMOTE_API_BASE_URL = process.env.REMOTE_SERVER || 'http://localhost:8080';

const getHeaders = (token) => ({
    Authorization: token,
    'Content-Type': 'application/json'
});

// 分页查询操作日志
const fetchOperationLogs = async (pageRequest, token) => {
    const response = await axios.post(
        `${REMOTE_API_BASE_URL}/api/operator/page`,
        pageRequest,
        { headers: getHeaders(token) }
    );
    return response.data;
};

// 获取当前用户操作日志
const fetchMyLogs = async (token) => {
    const response = await axios.get(
        `${REMOTE_API_BASE_URL}/api/operator/my`,
        { headers: getHeaders(token) }
    );
    return response.data;
};

// 获取指定日志详情
const fetchOperationLogById = async (id, token) => {
    const response = await axios.get(
        `${REMOTE_API_BASE_URL}/api/operator/${id}`,
        { headers: getHeaders(token) }
    );
    return response.data;
};