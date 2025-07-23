const Datastore = require('nedb');

const db = new Datastore({ filename: process.env.DB_PATH, autoload: true }); // 从 `.env` 获取数据库路径
db.loadDatabase((err) => {
    if (err) {
        console.error('Failed to load the database:', err);
    } else {
        console.log('Database loaded successfully');
    }
});

module.exports = db;
