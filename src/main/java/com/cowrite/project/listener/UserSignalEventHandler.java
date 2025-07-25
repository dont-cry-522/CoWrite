package com.cowrite.project.listener;

import com.cowrite.project.common.enums.NotificationLevel;
import com.cowrite.project.common.enums.NotificationType;
import com.cowrite.project.model.entity.Notification;
import com.cowrite.project.model.entity.Organization;
import com.cowrite.project.model.entity.OrganizationMember;
import com.cowrite.project.model.entity.User;
import com.cowrite.project.model.vo.UserVO;
import com.cowrite.project.service.EmailService;
import com.cowrite.project.service.NotificationService;
import com.cowrite.project.service.OrganizationMemberService;
import com.cowrite.project.service.OrganizationService;
import com.hibiscus.signal.Signals;
import com.hibiscus.signal.core.SignalContext;
import com.hibiscus.signal.spring.anno.SignalHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

import static com.cowrite.project.common.constants.CommonEventConstants.EVENT_INTER_MEDIATE_RESULT;
import static com.cowrite.project.common.constants.OrganizationConstants.ORG_STATUS_ACTIVE;
import static com.cowrite.project.common.constants.OrganizationConstants.ROLE_OWNER;
import static com.cowrite.project.common.constants.UserEventConstants.*;


@Component
public class UserSignalEventHandler {

    private final EmailService emailService;

    private final OrganizationService organizationService;

    private final OrganizationMemberService organizationMemberService;

    private final NotificationService notificationService;

    private final Signals signals;

    private static final Logger log = LoggerFactory.getLogger(UserSignalEventHandler.class);

    public UserSignalEventHandler(EmailService emailService, OrganizationService organizationService, OrganizationMemberService organizationMemberService, NotificationService notificationService, Signals signals) {
        this.emailService = emailService;
        this.organizationService = organizationService;
        this.organizationMemberService = organizationMemberService;
        this.notificationService = notificationService;
        this.signals = signals;
    }

    @SignalHandler(value = USER_LOGIN_EVENT, target = UserSignalEventHandler.class, methodName = "handleLogin")
    public void handleLogin(SignalContext signalContext) throws Exception {
        Map<String, Object> requestParams = signalContext.getAttributes();
        if (requestParams != null){
            for (Map.Entry<String, Object> params : requestParams.entrySet()){
                System.out.println("键: "+params.getKey() + "  值:" + params.getValue());
            }
        }
        UserVO result = (UserVO) signalContext.getAttributes().get(EVENT_INTER_MEDIATE_RESULT);
        System.out.println("函数结果: "+result);

        User user = (User) signalContext.getIntermediateValues().get(EVENT_INTER_MEDIATE_USER);
        System.out.println("用户登录事件处理: " + user + "登录");
        emailService.sendWelcomeEmail(user.getUsername(), user.getEmail());

        // send welcome notification
        notificationService.sendNotification(new Notification.Builder()
                .userId(user.getId())
                .type(NotificationType.SYSTEM)
                .title("欢迎登录")
                .content("欢迎您今日登录, 请开始今日的CoWrite使用之旅吧！")
                .level(NotificationLevel.WELCOME)
                .businessId(signalContext.getTraceId())
                .businessType(USER_LOGIN_EVENT)
                .sendTime(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());

    }

    @SignalHandler(value = USER_REGISTER_EVENT, target = UserSignalEventHandler.class, methodName = "handleRegister")
    public void handleRegister(SignalContext signalContext) throws Exception {
        Map<String, Object> requestParams = signalContext.getAttributes();
        if (requestParams != null){
            for (Map.Entry<String, Object> params : requestParams.entrySet()){
                System.out.println("键: "+params.getKey() + "  值:" + params.getValue());
            }
        }
        UserVO result = (UserVO) signalContext.getAttributes().get(EVENT_INTER_MEDIATE_RESULT);
        System.out.println("函数结果: "+result);

        User user = (User) signalContext.getIntermediateValues().get(EVENT_INTER_MEDIATE_USER);
        System.out.println("用户登录事件处理: " + user + "登陆成功");

        // send welcome email
        emailService.sendWelcomeEmail(user.getUsername(), user.getEmail());

        // send welcome notification for new registration
        notificationService.sendNotification(new Notification.Builder()
                .userId(user.getId())
                .type(NotificationType.SYSTEM)
                .title("欢迎加入 CoWrite！")
                .content("恭喜您成功注册 CoWrite, 成为了我们的用户！ 我们已为您创建了默认组织，您可以开始邀请团队成员共同协作了。")
                .level(NotificationLevel.WELCOME)
                .businessId(signalContext.getTraceId())
                .businessType("user.register")
                .sendTime(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());

        // init user Organization
        try {
            Organization org = Organization.builder()
                    .name(user.getUsername() + "'s Organization")
                    .description("Default organization for user: " + user.getUsername())
                    .ownerId(user.getId())
                    .status("active")
                    .published("no")
                    .maxMembers(10)
                    .currentMembers(1L)
                    .build();

            organizationService.save(org);

            OrganizationMember organizationMember = OrganizationMember.builder()
                    .role(ROLE_OWNER)
                    .organizationId(org.getId())
                    .userId(user.getId())
                    .status(ORG_STATUS_ACTIVE)
                    .joinedAt(LocalDateTime.now())
                    .build();

            organizationMemberService.save(organizationMember);
            log.info("user: {} init organization successful", user.getUsername());
        } catch (Exception e) {
            log.error("user: {} init organization failed: {}", user.getUsername(), e.getMessage(), e);
        }
    }
}