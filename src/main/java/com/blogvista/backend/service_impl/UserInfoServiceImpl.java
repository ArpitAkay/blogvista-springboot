package com.blogvista.backend.service_impl;

import com.blogvista.backend.dao.UserInfoUserDetails;
import com.blogvista.backend.entity.UserInfo;
import com.blogvista.backend.exception.RESTException;
import com.blogvista.backend.model.user_info.UserInfoResponse;
import com.blogvista.backend.repository.UserInfoRepository;
import com.blogvista.backend.service.UserInfoService;
import com.blogvista.backend.util.S3Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    private final UserInfoRepository userInfoRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final S3Util s3Util;

    public UserInfoServiceImpl(
            UserInfoRepository userInfoRepository,
            ModelMapper modelMapper,
            ObjectMapper objectMapper,
            S3Util s3Util
    ) {
        this.userInfoRepository = userInfoRepository;
        this.modelMapper = modelMapper;
        this.s3Util = s3Util;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public UserInfoResponse getUserInfoByEmail(
            String email
    ) throws RESTException {
        Optional<UserInfo> userInfoOptional = userInfoRepository.findByEmail(email);
        if (userInfoOptional.isPresent()) {
            UserInfo userInfo = userInfoOptional.get();
            return modelMapper.map(userInfo, UserInfoResponse.class);
        } else {
            throw new RESTException("User info not found with email " + email);
        }
    }

    @Override
    public List<UserInfoResponse> getAllUserInfo(
    ) {
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        return userInfoList.stream()
                .map(userInfo -> modelMapper.map(userInfo, UserInfoResponse.class))
                .toList();
    }

    @Override
    public UserInfoResponse updateUserInfo(
            MultipartFile multipartFile,
            String userInfoRequestInString
    ) throws JsonProcessingException, RESTException {
        UserInfoUserDetails userInfoUserDetails = (UserInfoUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        String email = userInfoUserDetails.getUsername();

        Optional<UserInfo> userInfoOptional = userInfoRepository.findByEmail(email);
        if (userInfoOptional.isPresent()) {
            UserInfo userInfo = userInfoOptional.get();
            Map<String, Object> userInfoData = objectMapper.readValue(userInfoRequestInString, Map.class);

            userInfoData.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(UserInfo.class, key);
                field.setAccessible(true);
                ReflectionUtils.setField(field, userInfo, value);
            });

            if(multipartFile != null) {
                String fileNameWithUUID = s3Util.uploadFileToS3Bucket(multipartFile);
                String profilePictureURL = s3Util.getImageUrlFromS3(fileNameWithUUID);
                userInfo.setProfileImageUrl(profilePictureURL);
                userInfo.setProfileImageName(fileNameWithUUID);
            }

            UserInfo userInfoSaved = userInfoRepository.save(userInfo);
            return modelMapper.map(userInfoSaved, UserInfoResponse.class);
        } else {
            throw new RESTException("User info not found with email " + email);
        }
    }
}
