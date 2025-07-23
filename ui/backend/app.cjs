const express = require('express');
const bodyParser = require('body-parser');
const userRoutes = require('./routes/userRoutes.cjs');
const authenticate = require('./middlewares/authMiddleware.cjs');
const cronJobs = require('./cronJobs/userStatsCronJob.cjs'); // 启动定时任务

const app = express();

// 路由配置示例
app.get('/info', (req, res) => {
    res.send('Hello, World!');
});

// 中间件
app.use(bodyParser.json());

// 路由
app.use('/api/users', authenticate, userRoutes);  // 认证保护路由

module.exports = app;
