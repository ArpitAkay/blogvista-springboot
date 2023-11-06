package com.blogvista.backend.service;

import com.blogvista.backend.entity.UserInfo;
import com.blogvista.backend.entity.Token;

import java.time.LocalDateTime;

public interface TokenService {
    Token generateVerificationToken(UserInfo userInfo, LocalDateTime expiresAt);
}
