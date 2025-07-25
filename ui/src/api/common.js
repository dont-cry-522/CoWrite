import request from '../utils/request'; // 引入公共请求封装

// 获取用户信息
export const getBackInfo = () => {
    return request.get('/info'); // 假设接口是 '/user/info'
}