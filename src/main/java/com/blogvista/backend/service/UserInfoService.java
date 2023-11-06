package com.blogvista.backend.service;

import com.blogvista.backend.exception.RESTException;
import com.blogvista.backend.model.user_info.UserInfoResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserInfoService {
    UserInfoResponse getUserInfoByEmail(
            String email
    ) throws RESTException;

    List<UserInfoResponse> getAllUserInfo();

    UserInfoResponse updateUserInfo(
            MultipartFile multipartFile,
            String userInfoRequestInString
    ) throws JsonProcessingException, RESTException;
}
