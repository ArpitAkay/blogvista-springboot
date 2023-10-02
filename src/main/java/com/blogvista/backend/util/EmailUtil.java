package com.blogvista.backend.util;

import com.blogvista.backend.entity.UserInfo;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class EmailUtil {

    public String sendVerificationEmail(UserInfo userInfo) throws MessagingException {
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
            message.setFrom(new InternetAddress("ak2400320@gmail.com"));
            message.setSubject("Registration Successful");
            message.setContent("<!DOCTYPE html>\n"
                    + "<html lang=\"en\">\n"
                    + "<head>\n"
                    + "    <meta charset=\"UTF-8\">\n"
                    + "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n"
                    + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                    + "    <title>Welcome Mail</title>\n"
                    + "</head>\n"
                    + "<body>\n"
                    + "    <h1>You are successfully registrated to our Blog Application!!!</h1>\n"
                    + "</body>\n"
                    + "</html>", "text/html");

            Transport.send(message);
            return "Success";
        }
        catch (Exception e) {
            return "Failed";
        }
    }
}
