package com.blogvista.backend.listener;

import com.blogvista.backend.entity.UserInfo;
import com.blogvista.backend.util.EmailUtil;
import jakarta.mail.MessagingException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SpringEventListener {
    private final EmailUtil emailUtil;

    public SpringEventListener(EmailUtil emailUtil) {
        this.emailUtil = emailUtil;
    }

    @EventListener
    public void listener(UserInfo userInfo) throws MessagingException {
        String status = emailUtil.sendVerificationEmail(userInfo);
        if(status.equals("Failed")) {
            emailUtil.sendVerificationEmail(userInfo);
        }
    }
}
