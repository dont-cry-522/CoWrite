package com.cowrite.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cowrite.project.model.entity.User;

public interface UserService extends IService<User> {

    /**
     * 发送邮箱验证码
     */
    Boolean sendVerificationCode(String email);
}