package com.blogvista.backend.controller;

import com.blogvista.backend.constant.Endpoint;
import com.blogvista.backend.exception.RESTException;
import com.blogvista.backend.model.user_info.UserInfoResponse;
import com.blogvista.backend.service.UserInfoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(Endpoint.USER_INFO_ENDPOINT)
public class UserInfoController {

    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping(Endpoint.GET_USER_INFO)
    public ResponseEntity<UserInfoResponse> getUserInfoByEmail(
            @RequestParam("email") String email
    ) throws RESTException {

        return new ResponseEntity<>(userInfoService.getUserInfoByEmail(email), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping(Endpoint.GET_ALL_USER_INFO)
    public ResponseEntity<List<UserInfoResponse>> getAllUserInfo() {
        return new ResponseEntity<>(userInfoService.getAllUserInfo(), HttpStatus.OK);
    }

    @PatchMapping(Endpoint.UPDATE_USER_INFO)
    public ResponseEntity<UserInfoResponse> updateUserInfo(
            @RequestPart("userInfoRequest") String userInfoRequestInString,
            @RequestPart(name = "profilePicture", required = false) MultipartFile multipartFile
            ) throws JsonProcessingException, RESTException {
        return new ResponseEntity<>(userInfoService
                .updateUserInfo(multipartFile, userInfoRequestInString), HttpStatus.OK);
    }
}
