package com.blogvista.backend.service;

import com.blogvista.backend.exception.RESTException;
import com.blogvista.backend.model.forget_password.ForgetPasswordRequest;
import com.blogvista.backend.model.login.LoginRequest;
import com.blogvista.backend.model.login.LoginResponse;
import com.blogvista.backend.model.user_info.UserInfoRequest;
import com.blogvista.backend.model.user_info.UserInfoResponse;
import com.blogvista.backend.model.verify_email.VerifyEmailRequest;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface AuthenticationService {
    LoginResponse login(
            LoginRequest loginRequest
    ) throws RESTException;

    UserInfoResponse signup(
            UserInfoRequest userInfoRequest
    ) throws RESTException;

    LoginResponse googleSignup(
            String idTokenString
    ) throws GeneralSecurityException, IOException, RESTException;

    String sendEmailVerificationMail(
            VerifyEmailRequest verifyEmailRequest
    ) throws RESTException, MessagingException;

    String sendForgetPasswordMail(
            VerifyEmailRequest verifyEmailRequest
    ) throws MessagingException, RESTException;

    String verifyEmailToken(
            String token
    ) throws RESTException;

    String verifyForgetPasswordToken(
            String token
    ) throws RESTException;

    String forgetPassword(
            ForgetPasswordRequest forgetPasswordRequest
    ) throws RESTException;

    LoginResponse generateAccessTokenViaRefreshToken(
            String refreshToken
    ) throws RESTException;
}
