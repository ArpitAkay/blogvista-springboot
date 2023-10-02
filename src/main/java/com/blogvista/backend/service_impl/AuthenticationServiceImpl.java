package com.blogvista.backend.service_impl;

import com.blogvista.backend.entity.Role;
import com.blogvista.backend.entity.UserInfo;
import com.blogvista.backend.enumeration.SignupType;
import com.blogvista.backend.exception.RESTException;
import com.blogvista.backend.model.login.LoginRequest;
import com.blogvista.backend.model.login.LoginResponse;
import com.blogvista.backend.model.user_info.UserInfoRequest;
import com.blogvista.backend.model.user_info.UserInfoResponse;
import com.blogvista.backend.model.verify_email.VerifyEmailRequest;
import com.blogvista.backend.repository.RoleRepository;
import com.blogvista.backend.repository.UserInfoRepository;
import com.blogvista.backend.service.AuthenticationService;
import com.blogvista.backend.util.EmailUtil;
import com.blogvista.backend.util.JwtUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;
    private final UserInfoRepository userInfoRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final EmailUtil emailUtil;
    private final Environment environment;

    public AuthenticationServiceImpl(
            PasswordEncoder passwordEncoder,
            ModelMapper modelMapper,
            RoleRepository roleRepository,
            UserInfoRepository userInfoRepository,
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            ApplicationEventPublisher applicationEventPublisher,
            EmailUtil emailUtil,
            Environment environment
    ) {
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
        this.userInfoRepository = userInfoRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.applicationEventPublisher = applicationEventPublisher;
        this.emailUtil = emailUtil;
        this.environment = environment;
    }

    @Override
    public LoginResponse login(
            LoginRequest loginRequest
    ) throws RESTException {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(), loginRequest.getPassword()));

        if (authentication.isAuthenticated()) {
            Optional<UserInfo> userInfo = userInfoRepository.findByEmail(loginRequest.getEmail());
            UserInfo userInfoSaved = userInfo.get();

            if (userInfoSaved.isVerified()) {
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setName(userInfoSaved.getFirstName() + " " + userInfoSaved.getLastName());
                loginResponse.setEmail(userInfoSaved.getEmail());
                loginResponse.setAccessToken(jwtUtil.generateToken(userInfoSaved));
                return loginResponse;
            } else {
                throw new RESTException("Please verify your account");
            }
        } else {
            throw new RESTException("Please enter valid credentials");
        }
    }

    @Override
    public UserInfoResponse signup(
            UserInfoRequest userInfoRequest
    ) throws RESTException {
        Optional<UserInfo> userInfoInDb = userInfoRepository.findByEmail(userInfoRequest.getEmail());

        if (userInfoInDb.isEmpty()) {
            UserInfo userInfo = modelMapper.map(userInfoRequest, UserInfo.class);
            userInfo.setVerified(true);
            userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
            Optional<Role> role = roleRepository.findById(102);
            if (role.isPresent()) {
                userInfo.getRoles().add(role.get());
            } else {
                throw new RESTException("User role not found in database");
            }
            userInfo.setType(SignupType.MANUAL);
            UserInfo userInfoSaved = userInfoRepository.save(userInfo);
            CompletableFuture.runAsync(() -> applicationEventPublisher.publishEvent(userInfoSaved));
            return modelMapper.map(userInfoSaved, UserInfoResponse.class);
        } else {
            throw new RESTException("Account with email " + userInfoRequest.getEmail() + " already exists");
        }
    }

    @Override
    public LoginResponse googleSignup(
            String idTokenString
    ) throws GeneralSecurityException, IOException, RESTException {
        try {
            HttpTransport transport = new NetHttpTransport();
            JsonFactory jsonFactory = new GsonFactory();

            String clientId = environment.getProperty("google.clientId");
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
                    .Builder(transport, jsonFactory)
                    .setAudience(Collections.singletonList(clientId))
                    .setIssuer("https://accounts.google.com")
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken != null) {
                Payload payload = idToken.getPayload();

                String email = payload.getEmail();
                UserInfo userInfo;

                Optional<UserInfo> userInfoInDb = userInfoRepository.findByEmail(email);
                if (userInfoInDb.isEmpty()) {
                    userInfo = new UserInfo();
                    userInfo.setFirstName((String) payload.get("given_name"));
                    userInfo.setLastName((String) payload.get("family_name"));
                    userInfo.setEmail(payload.getEmail());
                    userInfo.setVerified(payload.getEmailVerified());
                    Optional<Role> role = roleRepository.findById(102);
                    if (role.isPresent()) {
                        userInfo.getRoles().add(role.get());
                    } else {
                        throw new RESTException("User role not found in database");
                    }
                    userInfo.setType(SignupType.GOOGLE);
                } else {
                    userInfo = userInfoInDb.get();
                    userInfo.setFirstName((String) payload.get("given_name"));
                    userInfo.setLastName((String) payload.get("family_name"));
                    userInfo.setVerified(payload.getEmailVerified());
                }
                UserInfo userInfoSaved = userInfoRepository.save(userInfo);
                LoginResponse loginResponse = new LoginResponse();

                loginResponse.setName(userInfoSaved.getFirstName() + " " + userInfoSaved.getLastName());
                loginResponse.setEmail(userInfoSaved.getEmail());
                loginResponse.setAccessToken(jwtUtil.generateToken(userInfoSaved));
                return loginResponse;

            } else {
                throw new RESTException("Invalid ID token");
            }
        } catch (Exception e) {
            throw new RESTException(e.getMessage());
        }
    }

    @Override
    public String verifyEmail(
            VerifyEmailRequest verifyEmailRequest
    ) throws RESTException, MessagingException {
        Optional<UserInfo> userInfo = userInfoRepository.findByEmail(verifyEmailRequest.getEmail());

        if (userInfo.isPresent()) {
            emailUtil.sendVerificationEmail(userInfo.get());
            return "Verification email sent to " + verifyEmailRequest.getEmail();
        } else {
            throw new RESTException("Account with email " + verifyEmailRequest.getEmail() + " does not  exists");
        }
    }
}
