package com.blogvista.backend.controller;

import com.blogvista.backend.constant.Endpoint;
import com.blogvista.backend.exception.RESTException;
import com.blogvista.backend.model.login.LoginRequest;
import com.blogvista.backend.model.login.LoginResponse;
import com.blogvista.backend.model.user_info.UserInfoRequest;
import com.blogvista.backend.model.user_info.UserInfoResponse;
import com.blogvista.backend.model.verify_email.VerifyEmailRequest;
import com.blogvista.backend.service.AuthenticationService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping(Endpoint.AUTHENTICATION_ENDPOINT)
public class AuthenticationController {

    private final AuthenticationService userInfoService;

    public AuthenticationController(
            AuthenticationService userInfoService
    ) {
        this.userInfoService = userInfoService;
    }

    @PostMapping(Endpoint.LOGIN)
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest loginRequest
    ) throws RESTException {
        return new ResponseEntity<>(userInfoService.login(loginRequest), HttpStatus.OK);
    }

    @PostMapping(Endpoint.SIGN_UP)
    public ResponseEntity<UserInfoResponse> signup(
            @Valid @RequestBody UserInfoRequest userInfoRequest
    ) throws RESTException {
        return new ResponseEntity<>(userInfoService.signup(userInfoRequest), HttpStatus.CREATED);
    }

    @PostMapping(Endpoint.GOOGLE_SIGN_UP)
    public ResponseEntity<LoginResponse> googleSignup(
            @RequestParam("idTokenString") String idTokenString
    ) throws RESTException, GeneralSecurityException, IOException {
        return new ResponseEntity<>(userInfoService.googleSignup(idTokenString), HttpStatus.OK);
    }

    @PostMapping(Endpoint.VERIFY_EMAIL)
    public ResponseEntity<String> verifyEmail(
            @Valid @RequestBody VerifyEmailRequest verifyEmailRequest
    ) throws RESTException, MessagingException {
        return new ResponseEntity<>(userInfoService.verifyEmail(verifyEmailRequest), HttpStatus.OK);
    }
}