const express = require('express');
const router = express.Router();

// 插件分页列表
router.post('/page', async (req, res) => {
    const token = req.headers['authorization'];
    try {
        const result = await fetchPlugins(req.body, token);
        res.status(200).json(result);
    } catch (error) {
        console.error('Error fetching plugin list:', error.message);
        res.status(500).json({ message: 'Error fetching plugin list' });
    }
});

module.exports = router;


const axios = require('axios');

const REMOTE_API_BASE_URL = process.env.REMOTE_SERVER || 'http://localhost:8080';

const getHeaders = (token) => ({
    Authorization: token,
    'Content-Type': 'application/json',
});

// 获取插件列表（分页查询）
const fetchPlugins = async (pageRequest, token) => {
    try {
        const response = await axios.post(
            `${REMOTE_API_BASE_URL}/api/plugin/page`,
            pageRequest,
            { headers: getHeaders(token) }
        );
        return response.data;
    } catch (error) {
        console.error('Error fetching plugins:', error);
        throw error;
    }
};
