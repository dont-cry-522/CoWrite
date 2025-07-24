import request from '../utils/request';

/**
 * 分页获取操作日志列表
 * @param {Object} data - 分页参数、搜索关键词等（参考 PageRequest）
 */
export const getOperationLogs = (data = {}) => {
    return request.post('/operator/page', data);
};

/**
 * 获取当前用户的操作日志（最多 100 条）
 */
export const getMyOperationLogs = () => {
    return request.get('/operator/my');
};

/**
 * 获取单条操作日志详情
 * @param {number|string} id - 日志 ID
 */
export const getOperationLogById = (id) => {
    return request.get(`/operator/${id}`);
};
