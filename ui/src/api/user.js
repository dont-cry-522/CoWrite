// api/userApi.js
import request from '../utils/request';  // 引入封装的 axios 实例
import remoteRequest from '../utils/remoteRequest';

// 获取用户信息
export const getUserInfo = () => {
    return request.get('/users/info');  // 调用 Node.js 的 /api/users/info 路由
};

// 邮箱登录
export const login = (credentials) => {
    return request.post('/users/login/email', credentials);  // 调用 Node.js 的 /api/users/login/email 路由
};

// 邮箱注册
export const register = (userData) => {
    return request.post('/users/register/email', userData);  // 调用 Node.js 的 /api/users/register/email 路由
};

// 发送验证码
export const sendVerificationCode = (email) => {
    return request.get(`/users/send-code/${email}`);  // 调用 Node.js 的 /api/users/send-code/{email} 路由
};

// 上传头像
export const uploadAvatar = (file) => {
    const formData = new FormData();
    formData.append('file', file);  // 将文件加入 FormData，确保字段名称为 'file'

    return remoteRequest.post('/users/upload-avatar', formData, {
        headers: {
            'Content-Type': 'multipart/form-data' // 确保请求头为 multipart/form-data
        }
    });
};

// 修改用户信息
export const updateUserInfo = (user) => {
    return request.put('/users/update', user);  // 调用 Node.js 的 /api/users/update 路由
};

// 注销用户
export const deleteAccount = () => {
    return request.post('/users/delete-account');  // 调用 Node.js 的 /api/users/delete-account 路由
};
