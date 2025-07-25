package com.cowrite.project.config;

import com.cowrite.project.utils.ServletUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/*配置*/
@Component
public class ServerConfig {

    @Value("${cowrite.invite.base-url}")
    private String inviteBaseUrl;

    @Value("${cowrite.invite.font-url}")
    private String fontUrl;

    /**
     * 获取配置的邀请链接前缀
     */
    public String getInviteBaseUrl() {
        return inviteBaseUrl;
    }

    /**
     * 获取配置的邀请链接字体文件地址
     */
    public String getFontUrl() {
        return fontUrl;
    }

    /**
     * 获取完整的请求路径，包括：域名，端口，上下文访问路径
     *
     * @return 服务地址
     */
    public String getUrl() {
        HttpServletRequest request = ServletUtils.getRequest();
        return getDomain(request);
    }

    public static String getDomain(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        String contextPath = request.getServletContext().getContextPath();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).append(contextPath).toString();
    }
}
