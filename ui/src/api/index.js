// index.js - 统一导出
import * as userApi from './user.js';  // 导入用户相关接口
import * as commonApi from './common.js';  // 导入商品相关接口

// 统一暴露接口
export default {
    userApi,
    commonApi,
};
