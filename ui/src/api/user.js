import request from '../utils/request'; // 引入公共请求封装

// 获取用户信息
export const getUserInfo = () => {
    return request.get('/user/info'); // 假设接口是 '/user/info'
}

// 登录接口
export const login = (credentials) => {
    return request.post('/user/login', credentials); // 假设登录接口是 '/user/login'
}

// 注册接口
export const register = (userData) => {
    return request.post('/user/register', userData); // 假设注册接口是 '/user/register'
}
