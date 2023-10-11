package com.blogvista.backend.util;

import com.blogvista.backend.entity.UserInfo;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class EmailUtil {

    public String sendVerificationEmail(UserInfo userInfo, String subject, String emailTemplate) throws MessagingException {
        String from = "blogvista24@gmail.com";
        String to = userInfo.getEmail();

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.host", "smtp.gmail.com");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, "mdkw bsqw lrfp ubxi");
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setFrom(new InternetAddress("blogvista24@gmail.com"));
            message.setSubject(subject);

            message.setContent(emailTemplate, "text/html");

            Transport.send(message);
            return "Success";
        }
        catch (Exception e) {
            return "Failed";
        }
    }
}
