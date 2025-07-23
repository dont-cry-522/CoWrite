package com.cowrite.project.common.constants;

import com.cowrite.project.model.entity.User;

import java.util.UUID;

/**
 * 用户常量
 *
 * @author heathcetide
 */
public interface UserConstants {

    /**
     * 当前用户
     */
    String CURRENT_USERNAME = "current_username";

    /**
     * 匿名用户名
     */
    String DEFAULT_USERNAME = "anonymous";

    /**
     * 发送验证码
     */
    String SEND_EMAIL_CODE = "send_email_code";

    /**
     * 发送验证码发送时间
     */
    String SEND_EMAIL_CODE_SEND_TIME = "sendEmailCodeSendTime";

    /**
     * 发送间隔（分钟）
     */
    long SEND_INTERVAL = 1;

    /**
     * 验证码有效期（分钟）
     */
    Long SEND_EMAIL_CODE_TIME = 10L;

    /**
     * 登录token缓存key
     */
    String TOKEN_CACHE_KEY = "token_key";

    /**
     * 登录token缓存时间（分钟）
     */
    Long TOKEN_CACHE_TIME = 3L;

    /**
     * 用户缓存key
     */
    String USER_CACHE_KEY = "user_key";

    /**
     * 用户缓存时间（分钟）
     */
    Long USER_CACHE_TIME = 3L;

    String USER_STATUS_ACTIVE = "ACTIVE";

    String NEW_USER_PASSWORD = "123456";

    String EMPTY_PASSWORD_HASH = "moxie";

    String NEW_USER_NICKNAME = "Cow-" + UUID.randomUUID().toString().replace("-", "")
            .substring(UUID.randomUUID().toString().replace("-", "").length() - 10);

    String DEFAULT_USER = "https://cetide-1325039295.cos.ap-chengdu.myqcloud.com/west/default_user.png";

    String USER_LANGUAGE_DEFAULT = "zh-CN";

    Boolean USER_NOTIFICATIONS_DEFAULT = true;

    Boolean USER_THEME_DARK_DEFAULT = false;

    String USER_BIO_DEFAULT = "此用户很懒, 什么也没有留下.....";

    /**
     * 构建新用户
     */
    static User BuildNewUser(String email) {
        User user = new User();
        user.setUsername(NEW_USER_NICKNAME);
        user.setEmail(email);
        user.setPassword(NEW_USER_PASSWORD + EMPTY_PASSWORD_HASH);
        user.setAvatarUrl(DEFAULT_USER);
        user.setStatus(USER_STATUS_ACTIVE);
        user.setThemeDark(USER_THEME_DARK_DEFAULT);
        user.setEmailNotifications(USER_NOTIFICATIONS_DEFAULT);
        user.setLanguage(USER_LANGUAGE_DEFAULT);
        user.setBio(USER_BIO_DEFAULT);
        return user;
    }
}
