const userService = require('../services/userService.cjs');

const getUsers = async (req, res) => {
    try {
        const users = await userService.getAllUsers();
        res.status(200).json(users);
    } catch (error) {
        console.error('Error getting users:', error);
        res.status(500).json({ message: 'Internal Server Error' });
    }
};

const createUser = async (req, res) => {
    const { username, email } = req.body;
    try {
        const newUser = await userService.createUser(username, email);
        res.status(201).json(newUser);
    } catch (error) {
        console.error('Error creating user:', error);
        res.status(500).json({ message: 'Internal Server Error' });
    }
};

module.exports = {
    getUsers,
    createUser,
};
