const cron = require('node-cron');
const userService = require('../services/userService.cjs');

// 每天定时统计用户数量
cron.schedule('0 0 * * *', async () => {
    try {
        const users = await userService.getAllUsers();
        console.log(`Total users: ${users.length}`);
    } catch (error) {
        console.error('Error fetching user stats:', error);
    }
});
