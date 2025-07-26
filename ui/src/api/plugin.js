import request from '../utils/request'; // 引入公共请求封装

// 获取插件列表（分页查询）
export const fetchPlugins = (pageRequest) => {
    return request.post('/plugin/page', pageRequest);
};
