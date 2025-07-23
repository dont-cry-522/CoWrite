// services/remoteService.js
const axios = require('axios');

const REMOTE_API_BASE_URL = process.env.REMOTE_SERVER || 'http://remote-backend-api.com';  // 远程 Java 后端的 API 地址

// 用于从 Java 后端获取用户信息
const getUserInfo = async (userId) => {
    try {
        const response = await axios.get(`${REMOTE_API_BASE_URL}/user`, {
            params: { userId },  // 请求参数
            headers: {
                Authorization: `Bearer ${process.env.JWT_TOKEN}`  // 根据需要添加 token 或者其他认证信息
            }
        });
        return response.data;  // 返回用户信息
    } catch (error) {
        console.error('Error fetching user info:', error.message);
        throw error;  // 抛出错误，供调用方处理
    }
};

// 用于从 Java 后端登录
const loginUser = async (username, password) => {
    try {
        const response = await axios.post(`${REMOTE_API_BASE_URL}/login`, {
            username,
            password
        });
        return response.data;  // 返回登录后的数据，如 token 或用户信息
    } catch (error) {
        console.error('Error logging in:', error.message);
        throw error;
    }
};

module.exports = {
    getUserInfo,
    loginUser
};
