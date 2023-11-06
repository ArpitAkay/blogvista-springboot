package com.blogvista.backend.util;

import com.blogvista.backend.entity.Token;
import com.blogvista.backend.entity.UserInfo;
import com.blogvista.backend.exception.RESTException;
import com.blogvista.backend.repository.TokenRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TokenUtil {
    private final TokenRepository tokenRepository;

    public TokenUtil(
            TokenRepository tokenRepository
    ) {
        this.tokenRepository = tokenRepository;
    }

    public String validateToken(Token token) throws RESTException {
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RESTException("Your link is expired, please try again");
        }

        UserInfo userInfo = token.getUserInfo();
        if (userInfo == null) {
            throw new RESTException("Your link is invalid, please try again");
        }

        if (token.getConfirmedAt() != null) {
            throw new RESTException("Your link is invalid, please try again");
        }

        token.setConfirmedAt(LocalDateTime.now());
        tokenRepository.save(token);
        return "valid";
    }
}
