package com.cowrite.project.service.impl;


import com.cowrite.project.common.notification.SendNotifyStrategy;
import com.cowrite.project.common.notification.impl.SendNotificationImpl;
import com.cowrite.project.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * EmailServiceImpl
 *
 * @author heathcetide
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Resource
    @Qualifier("emailStrategy")
    private SendNotifyStrategy sendNotifyStrategy;

    @Resource
    private SendNotificationImpl sendNotificationService;



    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    /**
     * 带重试机制的邮件发送方法
     *
     * @param email         收件人邮箱
     * @param code          验证码
     * @param maxRetries    最大重试次数
     * @param retryInterval 每次重试的间隔（毫秒）
     */
    @Async("taskExecutor")
    public void sendEmailWithRetry(String email, String code, int maxRetries, long retryInterval) throws Exception {
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                // 发送邮件
                sendNotificationService.setSendNotifyStrategy(sendNotifyStrategy);
                sendNotificationService.sendMessage("请接收CoWrite的验证码" + code, email, "请接收CoWrite验证码");
                return; // 邮件发送成功，退出重试逻辑
            } catch (Exception e) {
                retryCount++;
                if (retryCount >= maxRetries) {
                    throw e; // 达到最大重试次数后抛出异常
                }
                // 等待一段时间再重试
                try {
                    Thread.sleep(retryInterval);
                } catch (InterruptedException ie) {
                    log.error("发送邮件失败：{}", ie.getMessage());
                    Thread.currentThread().interrupt(); // 恢复中断状态
                    throw new RuntimeException("邮件发送被中断");
                }
            }
        }
    }

    @Async("taskExecutor")
    public void sendWelcomeEmail(String username, String email) {
        try {
            // 发送欢迎邮件
            sendNotificationService.setSendNotifyStrategy(sendNotifyStrategy);
            sendNotificationService.sendWelcomeEmail(username, email);
        } catch (Exception e) {
            log.error("发送邮件失败："+e.getMessage());
        }
    }

}
