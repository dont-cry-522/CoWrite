const express = require('express');
const userController = require('../controllers/userController.cjs');
const router = express.Router();

// 获取所有用户
router.get('/', userController.getUsers);

// 创建新用户
router.post('/', userController.createUser);

module.exports = router;
