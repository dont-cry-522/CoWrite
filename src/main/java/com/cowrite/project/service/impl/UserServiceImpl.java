package com.cowrite.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowrite.project.common.context.AuthContext;
import com.cowrite.project.common.storage.FileStorageAdapter;
import com.cowrite.project.exception.BusinessException;
import com.cowrite.project.mapper.UserMapper;
import com.cowrite.project.model.dto.user.UserEmailRequest;
import com.cowrite.project.model.entity.User;
import com.cowrite.project.model.vo.UserVO;
import com.cowrite.project.service.EmailService;
import com.cowrite.project.service.UserService;
import com.cowrite.project.utils.CommonCheckUtils;
import com.cowrite.project.utils.JwtUtils;
import com.cowrite.project.utils.RedisUtils;
import com.hibiscus.signal.spring.anno.SignalEmitter;
import com.hibiscus.signal.spring.configuration.SignalContextCollector;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.cowrite.project.common.constants.CommonEventConstants.EVENT_INTER_MEDIATE_REQUEST;
import static com.cowrite.project.common.constants.UserConstants.*;
import static com.cowrite.project.common.constants.UserEventConstants.*;
import static com.cowrite.project.common.enums.ResponseCodeEnum.RATE_LIMIT_EXCEEDED;
import static com.cowrite.project.common.enums.ResponseCodeEnum.SYSTEM_ERROR;
import static com.cowrite.project.utils.CodeUtil.generateRandomCode;
import static com.cowrite.project.utils.CodeUtil.generateRedisKey;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final FileStorageAdapter fileStorageAdapter;

    private final EmailService emailService;

    private final RedisUtils redisUtils;

    private final UserMapper userMapper;

    private final JwtUtils jwtUtils;

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public UserServiceImpl(FileStorageAdapter fileStorageAdapter, EmailService emailService, RedisUtils redisUtils, UserMapper userMapper, JwtUtils jwtUtils) {
        this.fileStorageAdapter = fileStorageAdapter;
        this.emailService = emailService;
        this.redisUtils = redisUtils;
        this.userMapper = userMapper;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Boolean sendVerificationCode(String email) {
        String redisKeyCode = generateRedisKey(email, SEND_EMAIL_CODE);
        String redisKeySendTime = generateRedisKey(email, SEND_EMAIL_CODE_SEND_TIME);

        // 1.检查是否在发送间隔内（例如 1 分钟）
        Boolean isSent = redisUtils.setIfAbsent(redisKeySendTime, "1", SEND_INTERVAL, TimeUnit.MINUTES);
        if (Boolean.FALSE.equals(isSent)) {
            throw new BusinessException(RATE_LIMIT_EXCEEDED.code(), "验证码发送过于频繁，请稍后再试");
        }

        // 2️.生成验证码
        String code = generateRandomCode();
        try {
            // 3.1存储验证码（10 分钟有效期）
            redisUtils.set(redisKeyCode, code, SEND_EMAIL_CODE_TIME, TimeUnit.MINUTES);
            // 3.2设置发送时间间隔（1 分钟有效期）
            redisUtils.set(redisKeySendTime, "1", SEND_INTERVAL, TimeUnit.MINUTES);
            // 4.发送邮件（带重试机制）
            emailService.sendEmailWithRetry(email, code, 3, 1000);
            return true;
        } catch (RedisConnectionFailureException redisEx) {
            throw new RuntimeException("系统错误，请稍后重试");
        } catch (Exception e) {
            // 删除 Redis 中的数据，防止冗余
            redisUtils.delete(redisKeyCode);
            redisUtils.delete(redisKeySendTime);
            return false;
        }
    }

    /**
     * 用户邮箱注册
     */
    @Override
    @Transactional
    @SignalEmitter(USER_REGISTER_EVENT)
    public UserVO registerByEmail(UserEmailRequest userRegisterEmailRequest, HttpServletRequest request) {
        String email = userRegisterEmailRequest.getEmail();
        String redisKeyCode = generateRedisKey(email, SEND_EMAIL_CODE);
        if (Boolean.FALSE.equals(redisUtils.exists(redisKeyCode))) {
            throw new BusinessException(SYSTEM_ERROR.code(), "验证码过期, 请重新发送验证码");
        }
        if (!userRegisterEmailRequest.getCode().equals(redisUtils.get(redisKeyCode))) {
            throw new BusinessException(SYSTEM_ERROR.code(), "验证码错误, 请重新发送验证码");
        }
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getEmail, email);
        User userByEmail = userMapper.selectOne(userLambdaQueryWrapper);
        if (null != userByEmail) {
            throw new BusinessException(SYSTEM_ERROR.code(), "邮箱已被注册");
        }
        User user = BuildNewUser(userRegisterEmailRequest.getEmail());
        try {
            if (userMapper.insert(user) != 1) {
                throw new BusinessException(SYSTEM_ERROR.code(), "注册失败, 请联系站长");
            }
            SignalContextCollector.collect(EVENT_INTER_MEDIATE_USER, user);
            SignalContextCollector.collect(EVENT_INTER_MEDIATE_REQUEST, request);
            redisUtils.set(USER_CACHE_KEY + user.getEmail(), user.getEmail(), USER_CACHE_TIME);
            String token = jwtUtils.generateToken(user);
            // 缓存token和用户信息
            redisUtils.set(TOKEN_CACHE_KEY + user.getId(), token, TOKEN_CACHE_TIME);
            redisUtils.set(USER_CACHE_KEY + user.getEmail(), user.getEmail(), USER_CACHE_TIME);
            UserVO userVO = UserVO.convertToUserVO(user);
            userVO.setToken(token);
            return userVO;
        } catch (Exception e) {
            throw new BusinessException(SYSTEM_ERROR.code(), "操作失败, 请联系站长" + e.getMessage());
        }
    }

    @Override
    public User getCurrentUser() {
        return AuthContext.getCurrentUser();
    }

    /**
     * 用户邮箱登录
     */
    @Override
    @Transactional
    @SignalEmitter(USER_LOGIN_EVENT)
    public UserVO loginByEmail(UserEmailRequest userEmailRequest, HttpServletRequest request) {
        String email = userEmailRequest.getEmail();
        if (!CommonCheckUtils.isValidEmail(email)) {
            throw new BusinessException(SYSTEM_ERROR.code(), "邮箱格式不正确");
        }
        if (!userMapper.existsByEmail(email)) {
            throw new BusinessException(SYSTEM_ERROR.code(), "邮箱未注册");
        }
        String redisKeyCode = generateRedisKey(email, SEND_EMAIL_CODE);
        if (Boolean.FALSE.equals(redisUtils.exists(redisKeyCode))) {
            throw new BusinessException(SYSTEM_ERROR.code(), "操作失败，请重新发送验证码");
        }
        if (!userEmailRequest.getCode().equals(redisUtils.get(redisKeyCode))) {
            throw new BusinessException(SYSTEM_ERROR.code(), "验证码错误");
        }
        User user = userMapper.selectByEmail(userEmailRequest.getEmail());
        SignalContextCollector.collect(EVENT_INTER_MEDIATE_USER, user);
        SignalContextCollector.collect(EVENT_INTER_MEDIATE_REQUEST, request);
        String token = jwtUtils.generateToken(user);
        // 缓存token和用户信息
        redisUtils.set(TOKEN_CACHE_KEY + user.getId(), token, TOKEN_CACHE_TIME);
        redisUtils.set(USER_CACHE_KEY + user.getEmail(), user.getEmail(), USER_CACHE_TIME);
        UserVO userVO = UserVO.convertToUserVO(user);
        userVO.setToken(token);
        return userVO;
    }

    /**
     * 更新用户信息
     */
    @Override
    @Transactional
    public String updateUserInfo(User user) {
        if (user == null) {
            throw new IllegalArgumentException("error.username.empty");
        }

        User existingUser = getCurrentUser();
        // 验证当前用户是否有权限修改
        User currentUser = getUserByEmail(existingUser.getEmail());  // 使用 email 查询
        if (!Objects.equals(currentUser.getId(), existingUser.getId())) {
            throw new IllegalArgumentException("error.user.permission.denied");
        }
        // 确保 ID 不被修改
        user.setId(existingUser.getId());
        // 防止敏感字段被修改
        user.setPassword(existingUser.getPassword());

        // 使用 email 查询用户信息
        User updatedUser = userMapper.selectByEmail(user.getEmail());  // 使用 email 查询
        if (updatedUser == null) {
            throw new BusinessException(SYSTEM_ERROR.code(), "用户不存在");
        }

        // 删除旧缓存
        redisUtils.delete(USER_CACHE_KEY + updatedUser.getEmail());

        // 延时删除缓存
        scheduler.schedule(() -> {
            redisUtils.delete(USER_CACHE_KEY + updatedUser.getEmail());
        }, 500, TimeUnit.MILLISECONDS);

        // 更新数据库
        int updateCount = userMapper.updateById(user);  // 执行数据库更新
        if (updateCount == 0) {
            throw new BusinessException(SYSTEM_ERROR.code(), "更新失败");
        }

        // 清除缓存并重新添加
        redisUtils.delete(USER_CACHE_KEY + user.getEmail());
        redisUtils.set(USER_CACHE_KEY + user.getEmail(), user.getEmail(), USER_CACHE_TIME);
        String token = jwtUtils.generateToken(user);
        // 缓存token和用户信息
        redisUtils.set(TOKEN_CACHE_KEY + user.getId(), token, TOKEN_CACHE_TIME);
        redisUtils.set(USER_CACHE_KEY + user.getEmail(), user.getEmail(), USER_CACHE_TIME);
        return token;
    }

    /**
     * 用户退出登录
     */
    @Override
    public void logoutUser(String currentToken) {
        redisUtils.delete(TOKEN_CACHE_KEY + jwtUtils.getUserFromToken(currentToken).getId());
    }

    /**
     * 上传用户头像
     */
    @Override
    public String uploadAvatar(MultipartFile file) {
        String upload = fileStorageAdapter.upload(file);
        User user = userMapper.selectById(AuthContext.getCurrentUser().getId());
        fileStorageAdapter.delete(user.getAvatarUrl());
        user.setAvatarUrl(upload);
        userMapper.updateById(user);
        // 删除旧缓存
        redisUtils.delete(USER_CACHE_KEY + user.getEmail());

        // 延时删除缓存
        scheduler.schedule(() -> {
            redisUtils.delete(USER_CACHE_KEY + user.getEmail());
        }, 500, TimeUnit.MILLISECONDS);
        return upload;
    }

    /**
     * 用户申请注销账号
     */
    @Override
    @Transactional
    public void requestAccountDeletion() {
        User user = userMapper.selectById(AuthContext.getCurrentUser().getId());
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        user.setDeleted(true);
        userMapper.updateById(user);
    }

    /**
     * 通过 Email 获取用户信息
     */
    @Override
    public User getUserByEmail(String email) {
        // 使用 LambdaQueryWrapper 来构建查询条件
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);  // 使用 email 字段查询

        // 执行查询
        User user = this.getOne(queryWrapper);  // 使用 MyBatis-Plus 提供的 getOne() 方法查询

        // 如果未找到用户，返回 null 或抛出异常
        if (user == null) {
            throw new BusinessException(SYSTEM_ERROR.code(), "用户不存在");
        }

        return user;
    }
}