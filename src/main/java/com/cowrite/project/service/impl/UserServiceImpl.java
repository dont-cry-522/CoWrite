package com.cowrite.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowrite.project.common.notification.impl.EmailSendNotification;
import com.cowrite.project.mapper.UserMapper;
import com.cowrite.project.model.entity.User;
import com.cowrite.project.service.UserService;
import com.cowrite.project.utils.CodeCreateUtils;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public class UserServiceImpl extends ServiceImpl<com.cowrite.project.mapper.UserMapper, User> implements UserService {

    private final CodeCreateUtils codeCreateUtils;
    private final EmailSendNotification emailSendNotification;

    public UserServiceImpl(CodeCreateUtils codeCreateUtils, EmailSendNotification emailSendNotification) {
        this.codeCreateUtils = codeCreateUtils;
        this.emailSendNotification = emailSendNotification;
    }

    @Override
    public Boolean sendVerificationCode(String email) throws Exception {
        String code = codeCreateUtils.generateCode();
        codeCreateUtils.sendVerificationCode(email,code);
        emailSendNotification.sendRegisterCodeMessage(email, code);
        return true;
    }


    @Override
    public Boolean updateUser(User user) {
        if (user.getId() == null) {
            return false; // ID 不能为空
        }

        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, user.getId());

        // 只更新非空字段
        if (user.getUsername() != null) {
            updateWrapper.set(User::getUsername, user.getUsername());
        }
        if (user.getEmail() != null) {
            updateWrapper.set(User::getEmail, user.getEmail());
        }
        if (user.getPassword() != null) {
            updateWrapper.set(User::getPassword, user.getPassword());
        }
        if (user.getAvatarUrl() != null) {
            updateWrapper.set(User::getAvatarUrl, user.getAvatarUrl());
        }
        if (user.getStatus() != null) {
            updateWrapper.set(User::getStatus, user.getStatus());
        }
        if (user.getThemeDark() != null) {
            updateWrapper.set(User::getThemeDark, user.getThemeDark());
        }
        if (user.getEmailNotifications() != null) {
            updateWrapper.set(User::getEmailNotifications, user.getEmailNotifications());
        }
        if (user.getLanguage() != null) {
            updateWrapper.set(User::getLanguage, user.getLanguage());
        }
        if (user.getBio() != null) {
            updateWrapper.set(User::getBio, user.getBio());
        }

        // 执行更新
        boolean success = this.update(updateWrapper);
        return success ? Boolean.TRUE : Boolean.FALSE;
    }
}