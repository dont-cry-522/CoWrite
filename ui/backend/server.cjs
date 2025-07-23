const express = require('express');
const Datastore = require('nedb');  // 引入 NeDB
require('dotenv').config();

// 初始化 Express 应用
const app = express();
const port = 3000;

// 创建一个 NeDB 内存数据库
const db = new Datastore({ inMemoryOnly: true }); // 只在内存中保存数据

// 测试数据库连接
function testDatabaseConnection() {
    db.insert({ message: 'Test connection' }, function (err, newDoc) {
        if (err) {
            console.error('Error connecting to the database:', err.message);
        } else {
            console.log('Database connection is working!');
        }
    });
}

// 启动服务器时测试数据库连接
testDatabaseConnection();

// 定义一个简单的路由来获取所有用户
app.get('/users', (req, res) => {
    db.find({}, (err, docs) => {
        if (err) {
            console.error('Error fetching users:', err.message);
            res.status(500).send('Internal Server Error');
        } else {
            res.json(docs);  // 返回查询到的用户数据
        }
    });
});

// 简单的 hello 路由
app.get('/hello', (req, res) => {
    res.send('Hello');
});

// 启动服务器
app.listen(port, () => {
    console.log(`Server is running on http://localhost:${port}`);
});
