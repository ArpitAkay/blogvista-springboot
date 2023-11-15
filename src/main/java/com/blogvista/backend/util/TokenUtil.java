package com.blogvista.backend.util;

import com.blogvista.backend.entity.Token;
import com.blogvista.backend.entity.UserInfo;
import com.blogvista.backend.exception.RESTException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TokenUtil {

    public String validateToken(Token token) throws RESTException {
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RESTException("Your link is expired, please try again");
        }

        UserInfo userInfo = token.getUserInfo();
        if (userInfo == null) {
            throw new RESTException("Your link is invalid, please try again");
        }

        return "valid";
    }
}
