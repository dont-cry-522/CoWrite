package com.cowrite.project.utils;

/**
 * 常规格式检测类
 */
public class CommonCheckUtils {

    /**
     * 验证邮箱格式是否正确
     */
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
        return email != null && email.matches(emailRegex);
    }

}
