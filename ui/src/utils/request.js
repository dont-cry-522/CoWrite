import axios from 'axios';
// 通知组件的优化版
const notify = {
    show: (type, message) => {
        const div = document.createElement('div');
        div.classList.add('notify', type); // 添加类型类名
        div.textContent = message;

        // 给通知添加动画
        div.style.animation = 'fadeIn 1s ease-out';

        document.body.appendChild(div);

        // 设置定时器，3秒后自动消失
        setTimeout(() => {
            div.style.animation = 'fadeOut 1s ease-in'; // 使通知淡出
            setTimeout(() => div.remove(), 100); // 动画结束后移除元素
        }, 3000);
    },

    success: (message) => {
        notify.show('success', `Success: ${message}`);
    },

    error: (message) => {
        notify.show('error', `Error: ${message}`);
    },

    warning: (message) => {
        notify.show('warning', `Warning: ${message}`);
    }
};

// 创建 axios 实例
const instance = axios.create({
    baseURL: 'http://localhost:3000/api', // 根据实际情况设置 API 地址
    timeout: 10000, // 设置请求超时
});

// 请求拦截器
instance.interceptors.request.use(
    config => {
        // 获取 token
        const token = localStorage.getItem('token');

        // 如果有 token，添加到请求头
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
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
        if (error.response) {
            const status = error.response.status;
            if (status === 401) {
                // 401: 未授权，令牌过期或无效
                notify.error('令牌无效或已过期，请重新登录');
                // 清除 token，并且跳转到登录页面
                localStorage.removeItem('token');
                // 根据需要，你可以使用以下代码进行重定向到登录页面（例如使用 react-router）
                window.location.href = '/login'; // 假设你有一个 /login 路由
            } else if (status === 403) {
                // 403: 禁止访问
                notify.error('您没有权限访问此资源');
            } else if (status === 404) {
                // 404: 资源未找到
                notify.error('请求的资源未找到');
            } else if (status === 500) {
                // 500: 服务器错误
                notify.error('服务器内部错误，请稍后再试');
            } else {
                // 其他错误
                notify.error('请求失败，请重试');
            }
        } else {
            notify.error('网络错误，请检查网络连接');
        }
        return Promise.reject(error);
    }
);

export default instance;

// 添加通知样式
const style = document.createElement('style');
style.textContent = `
.notify {
    position: fixed;
    bottom: 20px;
    left: 50%;
    transform: translateX(-50%);
    padding: 12px 20px;
    border-radius: 8px;
    color: #fff;
    font-weight: bold;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    z-index: 9999;
    opacity: 1;
}

.success {
    background-color: #4CAF50; /* 绿色 */
}

.error {
    background-color: #f44336; /* 红色 */
}

.warning {
    background-color: #ff9800; /* 橙色 */
}

@keyframes fadeIn {
    from {
        opacity: 0;
        transform: translateX(-50%) translateY(20px);
    }
    to {
        opacity: 1;
        transform: translateX(-50%) translateY(0);
    }
}

@keyframes fadeOut {
    from {
        opacity: 1;
        transform: translateX(-50%) translateY(0);
    }
    to {
        opacity: 0;
        transform: translateX(-50%) translateY(20px);
    }
}
`;

document.head.appendChild(style); // 将样式添加到页面中
