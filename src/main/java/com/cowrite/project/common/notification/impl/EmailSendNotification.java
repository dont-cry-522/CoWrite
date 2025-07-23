package com.cowrite.project.common.notification.impl;

import com.cowrite.project.common.enums.NotificationLevel;
import com.cowrite.project.common.notification.SendNotifyStrategy;
import com.cowrite.project.model.entity.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
@Qualifier("emailStrategy")
public class EmailSendNotification implements SendNotifyStrategy {

    private static final Logger log = LoggerFactory.getLogger(EmailSendNotification.class);

    private final JavaMailSender mailSender;

    private final SpringTemplateEngine templateEngine;

    public EmailSendNotification(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void send(Notification notification) throws Exception {
        log.warn("邮件无法发送notification");
    }

    @Override
    public void send(String content, String email, String subject) throws Exception {
        try {
            // 根据通知级别选择模板和内容
            String templateName = getTemplateNameByLevel(NotificationLevel.Verification);
            Context context = new Context();
            context.setVariable("verificationCode", content);
            context.setVariable("email", email); // 如果需要使用用户信息，可以在模板中使用

            // 渲染模板
            String emailContent = templateEngine.process(templateName, context);

            // 发送邮件
            sendMessage(email, subject, emailContent);
        } catch (Exception e) {
            throw new RuntimeException("发送邮件通知失败", e);
        }
    }

    @Override
    public void sendMessage(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        message.setFrom("heath-cetide@zohomail.com");
        // 设置带有显示名称的 From 地址
        String officialFrom = "CoWrite <heath-cetide@zohomail.com>";
        helper.setFrom(new InternetAddress(officialFrom));
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        mailSender.send(message);
    }

    @Override
    public void sendWelcomeMessage(String to, String username) throws MessagingException {
        // 创建邮件内容
        Context context = new Context();
        context.setVariable("username", username); // 设置动态变量
        // 渲染模板
        String body = templateEngine.process("welcome-email", context);
        // 创建邮件
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        message.setFrom("heath-cetide@zohomail.com");
        // 设置带有显示名称的 From 地址
        String officialFrom = "CoWrite <heath-cetide@zohomail.com>";
        helper.setFrom(new InternetAddress(officialFrom));
        helper.setTo(to);
        helper.setSubject("欢迎加入CoWrite！");
        helper.setText(body, true);
        // 发送邮件
        mailSender.send(message);
    }

    @Override
    public void sendRegisterCodeMessage(String to, String code) throws MessagingException {
        // 创建邮件内容
        Context context = new Context();
        context.setVariable("verificationCode", code); // 设置动态变量
        // 渲染模板
        String body = templateEngine.process("send-email-register", context);
        // 创建邮件
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        message.setFrom("heath-cetide@zohomail.com");
        // 设置带有显示名称的 From 地址
        String officialFrom = "CoWrite <heath-cetide@zohomail.com>";
        helper.setFrom(new InternetAddress(officialFrom));
        helper.setTo(to);
        helper.setSubject("欢迎加入CoWrite！");
        helper.setText(body, true);
        // 发送邮件
        mailSender.send(message);
    }

    private String getTemplateNameByLevel(NotificationLevel level) {
        // 根据通知级别返回不同的模板
        switch (level) {
            case WELCOME:
                return "welcome-email";
            case Verification:
                return "send-email-register";
            case IMPORTANT:
                return "important-notification";
            case URGENT:
                return "urgent-notification";
            case NORMAL:
                return "normal-notification";
            default:
                return "notification";
        }
    }
}
