package com.blogvista.backend.listener;

import com.blogvista.backend.entity.UserInfo;
import com.blogvista.backend.entity.Token;
import com.blogvista.backend.service.TokenService;
import com.blogvista.backend.util.EmailUtil;
import com.blogvista.backend.util.JwtUtil;
import jakarta.mail.MessagingException;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

@Component
public class SpringEventListener {
    private final EmailUtil emailUtil;
    private final TemplateEngine templateEngine;
    private final TokenService tokenService;
    private final JwtUtil jwtUtil;
    private final Environment environment;

    public SpringEventListener(
            EmailUtil emailUtil,
            TemplateEngine templateEngine,
            TokenService tokenService,
            JwtUtil jwtUtil, Environment environment
    ) {
        this.emailUtil = emailUtil;
        this.templateEngine = templateEngine;
        this.tokenService = tokenService;
        this.jwtUtil = jwtUtil;
        this.environment = environment;
    }

    @EventListener
    public void listener(
            UserInfo userInfo
    ) throws MessagingException {
        Token token = tokenService.generateVerificationToken(userInfo, LocalDateTime.now().plusMinutes(30));
        Context context = new Context();
        context.setVariable("name", userInfo.getFirstName() + " " + userInfo.getLastName());
        context.setVariable("verificationLink",
                environment.getProperty("url.emailVerify") + token.getVerificationToken()
                        + "&authToken=" + jwtUtil.generateTokenFor30Minutes(userInfo));
        String emailTemplate = templateEngine.process("EmailVerification", context);

        String status = emailUtil.sendVerificationEmail(userInfo, "Account Verification", emailTemplate);
        if (status.equals("Failed")) {
            emailUtil.sendVerificationEmail(userInfo, "Account Verification", emailTemplate);
        }
    }
}
