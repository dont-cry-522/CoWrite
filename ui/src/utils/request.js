import axios from 'axios';

// 获取全局的通知系统
const notify = window.Vue.prototype.$notify;

// 创建 axios 实例
const instance = axios.create({
    baseURL: 'https://api.example.com', // 根据实际情况设置 API 地址
    timeout: 10000, // 设置请求超时
});

// 请求拦截器
instance.interceptors.request.use(
    config => {
        // 在请求发送之前，可以在这里做一些处理（如添加认证信息等）
        // 例如：config.headers['Authorization'] = `Bearer ${token}`;
        return config;
    },
    error => {
        // 请求错误时显示通知
        notify.error('请求失败，请检查网络或重试');
        return Promise.reject(error);
    }
);

// 响应拦截器
instance.interceptors.response.use(
    response => {
        // 成功的响应处理
        if (response.data && response.data.success) {
            // 如果服务器返回的结果表示成功
            notify.success(response.data.message || '操作成功');
        } else {
            // 如果返回的结果有问题，显示警告
            notify.warning(response.data.message || '警告：操作可能有问题');
        }
        return response.data;
    },
    error => {
        // 错误的响应处理
        if (error.response) {
            // 如果服务器返回错误响应（如 400、500 错误等）
            const status = error.response.status;
            if (status === 404) {
                notify.error('请求的资源未找到');
            } else if (status === 500) {
                notify.error('服务器内部错误，请稍后再试');
            } else {
                notify.error('请求失败，请重试');
            }
        } else {
            // 如果没有响应，通常是网络问题或超时
            notify.error('网络错误，请检查网络连接');
        }
        return Promise.reject(error);
    }
);

export default instance;
