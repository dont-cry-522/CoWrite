const express = require('express');
const bodyParser = require('body-parser');
const userRoutes = require('./routes/userRoutes.cjs');
const notificationRoutes = require('./routes/notificationRoutes.cjs');
const operatorRoutes = require('./routes/operator.cjs');
const cors = require('cors');
const authenticate = require('./middlewares/authMiddleware.cjs');
const cronJobs = require('./cronJobs/userStatsCronJob.cjs'); // 启动定时任务

const app = express();

app.use(cors())
// 路由配置示例
app.get('/api/info', (req, res) => {
    res.send('PONG');
});

// 中间件
app.use(bodyParser.json());

// 路由
app.use('/api/users', userRoutes);
app.use('/api/notifications', notificationRoutes);
app.use('/api/operator', operatorRoutes);

module.exports = app;
