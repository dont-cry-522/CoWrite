package com.cowrite.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowrite.project.mapper.UserMapper;
import com.cowrite.project.model.entity.User;
import com.cowrite.project.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService  {

    @Override
    public Boolean sendVerificationCode(String email) {
        return null;
    }
}