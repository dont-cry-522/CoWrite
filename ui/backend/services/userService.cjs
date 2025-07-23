const db = require('../config/db.cjs');

// 获取所有用户
const getAllUsers = () => {
    return new Promise((resolve, reject) => {
        db.find({}, (err, docs) => {
            if (err) {
                return reject(err);
            }
            resolve(docs);
        });
    });
};

// 创建新用户
const createUser = (username, email) => {
    return new Promise((resolve, reject) => {
        const newUser = { username, email };
        db.insert(newUser, (err, newDoc) => {
            if (err) {
                return reject(err);
            }
            resolve(newDoc);
        });
    });
};

module.exports = {
    getAllUsers,
    createUser,
};
