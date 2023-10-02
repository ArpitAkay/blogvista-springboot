package com.blogvista.backend.controller;

import com.blogvista.backend.constant.Endpoint;
import com.blogvista.backend.exception.RESTException;
import com.blogvista.backend.model.user_info.UserInfoResponse;
import com.blogvista.backend.service.UserInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(Endpoint.USER_INFO_ENDPOINT)
public class UserInfoController {

    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @GetMapping(Endpoint.GET_USER_INFO_ENDPOINT)
    public ResponseEntity<UserInfoResponse> getUserInfoByEmail(
            @RequestParam("email") String email
    ) throws RESTException {
        return new ResponseEntity<>(userInfoService.getUserInfoByEmail(email), HttpStatus.OK);
    }

    @GetMapping(Endpoint.GET_ALL_USER_INFO_ENDPOINT)
    public ResponseEntity<List<UserInfoResponse>> getAllUserInfo() {
        return new ResponseEntity<>(userInfoService.getAllUserInfo(), HttpStatus.OK);
    }
}
