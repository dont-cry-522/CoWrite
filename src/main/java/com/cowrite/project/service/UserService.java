package com.cowrite.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cowrite.project.common.anno.Loggable;
import com.cowrite.project.common.enums.LogType;
import com.cowrite.project.model.dto.user.UserEmailRequest;
import com.cowrite.project.model.entity.User;
import com.cowrite.project.model.vo.UserVO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface UserService extends IService<User> {

    /**
     * 发送邮箱验证码
     */
    Boolean sendVerificationCode(String email) throws Exception;

    /**
     * 处理邮箱注册逻辑
     */
    UserVO registerByEmail(UserEmailRequest userRegisterEmailRequest, HttpServletRequest request);

    /**
     * 获取当前登录用户
     */
    User getCurrentUser();

    /**
     * 用户登录
     */
    UserVO loginByEmail(UserEmailRequest userEmailRequest, HttpServletRequest request);

    /**
     * 修改用户信息
     */
    String updateUserInfo(User user);

    /**
     * 用户退出登录
     */
    void logoutUser(String currentToken);

    /**
     * 用户上传头像
     */
    String uploadAvatar(MultipartFile file);

    /**
     * 用户申请注销账号
     */
    void requestAccountDeletion();

    /**
     * 根据邮箱获取用户信息
     */
    User getUserByEmail(String email);
}