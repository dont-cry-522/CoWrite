// routes/userRoutes.cjs
const express = require('express');
const router = express.Router();
const multer = require('multer');
const upload = multer({ dest: 'uploads/' });

// 获取所有用户
router.get('/send-code/:email', async (req, res) => {
    const { email } = req.params;
    try {
        const result = await sendVerificationCode(email);
        res.status(200).json(result);  // 转发结果
    } catch (error) {
        console.error('Error in /send-code:', error.message);

        // Check if it's an Unauthorized or Forbidden error
        if (error.response && error.response.status === 401) {
            return res.status(401).json({ message: 'Unauthorized: Invalid token or session expired' });
        }
        if (error.response && error.response.status === 403) {
            return res.status(403).json({ message: 'Forbidden: Access denied' });
        }

        res.status(500).json({ message: 'Error sending verification code' });
    }
});

// 邮箱注册账号
router.post('/register/email', async (req, res) => {
    const { code, email } = req.body;
    try {
        const result = await registerUserByEmail({ code, email });
        res.status(201).json(result);  // 转发注册结果
    } catch (error) {
        console.error('Error in /register/email:', error.message);

        // Handle specific error codes
        if (error.response && error.response.status === 401) {
            return res.status(401).json({ message: 'Unauthorized: Invalid code or session expired' });
        }

        res.status(500).json({ message: 'Error registering user by email' });
    }
});

// 根据 token 获取用户信息
router.get('/info', async (req, res) => {
    try {
        const result = await getUserInfoByToken();
        res.status(200).json(result);  // 转发用户信息
    } catch (error) {
        console.error('Error in /info:', error.message);

        // Handle specific error codes
        if (error.response && error.response.status === 401) {
            return res.status(401).json({ message: 'Unauthorized: Token expired or invalid' });
        }

        res.status(500).json({ message: 'Error fetching user info by token' });
    }
});

// 邮箱登录
router.post('/login/email', async (req, res) => {
    const { email, code } = req.body;
    try {
        const result = await loginUserByEmail({ email, code });
        res.status(200).json(result);  // 转发登录结果（如 token）
    } catch (error) {
        console.error('Error in /login/email:', error.message);

        // Handle specific error codes
        if (error.response && error.response.status === 401) {
            return res.status(401).json({ message: 'Unauthorized: Invalid email or code' });
        }

        res.status(500).json({ message: 'Error logging in user by email' });
    }
});

// 用户退出登录
router.post('/logout', async (req, res) => {
    try {
        const result = await logoutUser();
        res.status(200).json(result);  // 转发退出结果
    } catch (error) {
        console.error('Error in /logout:', error.message);

        // Handle specific error codes
        if (error.response && error.response.status === 401) {
            return res.status(401).json({ message: 'Unauthorized: Invalid session' });
        }

        res.status(500).json({ message: 'Error logging out user' });
    }
});

// 修改用户信息
router.put('/update', async (req, res) => {
    const user = req.body;
    const token = req.headers['authorization'];
    if (!token) {
        return res.status(403).json({ message: 'No token provided' });
    }

    try {
        const result = await updateUserInfo(user, token);
        res.status(200).json(result);  // 转发更新结果
    } catch (error) {
        console.error('Error in /update:', error.message);

        // Handle specific error codes
        if (error.response && error.response.status === 401) {
            return res.status(401).json({ message: 'Unauthorized: Invalid token or expired session' });
        }

        res.status(500).json({ message: 'Error updating user info' });
    }
});

// 注销用户
router.post('/delete-account', async (req, res) => {
    try {
        const result = await deleteAccount();
        res.status(200).json(result);  // 转发注销结果
    } catch (error) {
        console.error('Error in /delete-account:', error.message);

        // Handle specific error codes
        if (error.response && error.response.status === 401) {
            return res.status(401).json({ message: 'Unauthorized: Invalid token or expired session' });
        }

        res.status(500).json({ message: 'Error deleting account' });
    }
});

module.exports = router;

const axios = require('axios');

const REMOTE_API_BASE_URL = process.env.REMOTE_SERVER || 'http://localhost:8080';  // Java 后端的 API 地址

// 设置请求头部（带上 JWT token 或其他身份认证信息）
const getHeaders = (token) => {
    return {
        Authorization: token,  // 使用从 localStorage 获取到的 token
        'Content-Type': 'application/json',
    };
};

// 发送验证码
const sendVerificationCode = async (email) => {
    try {
        const response = await axios.get(`${REMOTE_API_BASE_URL}/api/users/send-code/${email}`);
        return response.data;  // 返回成功或失败的标志
    } catch (error) {
        console.error('Error sending verification code:', error.message);
        throw error;
    }
};

// 邮箱注册账号
const registerUserByEmail = async (userEmailRequest) => {
    try {
        const response = await axios.post(`${REMOTE_API_BASE_URL}/api/users/register/email`, userEmailRequest);
        return response.data;  // 返回注册后的用户信息或状态
    } catch (error) {
        console.error('Error registering user by email:', error.message);
        throw error;
    }
};

// 根据 token 获取用户信息
const getUserInfoByToken = async () => {
    try {
        const response = await axios.get(`${REMOTE_API_BASE_URL}/api/users/info`, {
            headers: getHeaders(),
        });
        return response.data;  // 返回用户信息
    } catch (error) {
        console.error('Error fetching user info by token:', error.message);
        throw error;
    }
};

// 邮箱登录
const loginUserByEmail = async (userEmailRequest) => {
    try {
        const response = await axios.post(`${REMOTE_API_BASE_URL}/api/users/login/email`, userEmailRequest);
        return response.data;  // 返回登录后的数据，如 token 或用户信息
    } catch (error) {
        console.error('Error logging in user by email:', error.message);
        throw error;
    }
};

// 用户退出登录
const logoutUser = async () => {
    try {
        const response = await axios.post(`${REMOTE_API_BASE_URL}/api/users/logout`, {}, {
            headers: getHeaders(),
        });
        return response.data;  // 返回退出成功的信息
    } catch (error) {
        console.error('Error logging out user:', error.message);
        throw error;
    }
};

// 修改用户信息
const updateUserInfo = async (user, token) => {
    try {
        const response = await axios.put(`${REMOTE_API_BASE_URL}/api/users/update`, user, {
            headers: getHeaders(token),
        });
        return response.data;  // 返回修改后的用户信息
    } catch (error) {
        console.error('Error updating user info:', error.message);
        throw error;
    }
};

// 注销用户
const deleteAccount = async () => {
    try {
        const response = await axios.post(`${REMOTE_API_BASE_URL}/api/users/delete-account`, {}, {
            headers: getHeaders(),
        });
        return response.data;  // 返回注销的结果
    } catch (error) {
        console.error('Error deleting account:', error.message);
        throw error;
    }
};