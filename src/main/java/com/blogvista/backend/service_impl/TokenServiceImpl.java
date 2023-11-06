package com.blogvista.backend.service_impl;

import com.blogvista.backend.entity.Token;
import com.blogvista.backend.entity.UserInfo;
import com.blogvista.backend.repository.TokenRepository;
import com.blogvista.backend.service.TokenService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;

    public TokenServiceImpl(
            TokenRepository tokenRepository
    ) {
        this.tokenRepository = tokenRepository;
    }

    public Token generateVerificationToken(UserInfo userInfo, LocalDateTime expiresAt) {
        Token token = new Token();
        token.setVerificationToken(UUID.randomUUID().toString());
        token.setIssuedAt(LocalDateTime.now());
        token.setExpiresAt(expiresAt);
        token.setUserInfo(userInfo);
        return tokenRepository.save(token);
    }
}
