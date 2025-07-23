package com.cowrite.project.listener;

import com.cowrite.project.model.entity.User;
import com.cowrite.project.service.EmailService;
import com.hibiscus.signal.Signals;
import com.hibiscus.signal.core.SignalContext;
import com.hibiscus.signal.spring.anno.SignalHandler;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.cowrite.project.common.constants.CommonEventConstants.EVENT_INTER_MEDIATE_RESULT;
import static com.cowrite.project.common.constants.UserEventConstants.*;


@Component
public class UserSignalEventHandler {

    private final EmailService emailService;

    private final Signals signals;

    public UserSignalEventHandler(EmailService emailService, Signals signals) {
        this.emailService = emailService;
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
        String result = (String) signalContext.getAttributes().get(EVENT_INTER_MEDIATE_RESULT);
        System.out.println("函数结果: "+result);

        User user = (User) signalContext.getIntermediateValues().get(EVENT_INTER_MEDIATE_USER);
        System.out.println("用户登录事件处理: " + user + "登录");
        emailService.sendWelcomeEmail(user.getUsername(), user.getEmail());
    }

    @SignalHandler(value = USER_REGISTER_EVENT, target = UserSignalEventHandler.class, methodName = "handleRegister")
    public void handleRegister(SignalContext signalContext) throws Exception {
        Map<String, Object> requestParams = signalContext.getAttributes();
        if (requestParams != null){
            for (Map.Entry<String, Object> params : requestParams.entrySet()){
                System.out.println("键: "+params.getKey() + "  值:" + params.getValue());
            }
        }
        String result = (String) signalContext.getAttributes().get(EVENT_INTER_MEDIATE_RESULT);
        System.out.println("函数结果: "+result);

        User user = (User) signalContext.getIntermediateValues().get(EVENT_INTER_MEDIATE_USER);
        System.out.println("用户登录事件处理: " + user + "登陆成功");
        emailService.sendWelcomeEmail(user.getUsername(), user.getEmail());
    }
}
