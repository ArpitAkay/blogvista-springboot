package com.blogvista.backend.service_impl;

import com.blogvista.backend.entity.UserInfo;
import com.blogvista.backend.exception.RESTException;
import com.blogvista.backend.model.user_info.UserInfoResponse;
import com.blogvista.backend.repository.UserInfoRepository;
import com.blogvista.backend.service.UserInfoService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    private final UserInfoRepository userInfoRepository;
    private final ModelMapper modelMapper;

    public UserInfoServiceImpl(UserInfoRepository userInfoRepository, ModelMapper modelMapper) {
        this.userInfoRepository = userInfoRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserInfoResponse getUserInfoByEmail(
            String email
    ) throws RESTException {
        Optional<UserInfo> userInfoOptional = userInfoRepository.findByEmail(email);
        if (userInfoOptional.isPresent()) {
            System.out.println(userInfoOptional.get());
            UserInfo userInfo = userInfoOptional.get();
            return modelMapper.map(userInfo, UserInfoResponse.class);
        }
        else {
            throw new RESTException("User info not found with email " + email);
        }
    }

    @Override
    public List<UserInfoResponse> getAllUserInfo() {
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        return userInfoList.stream()
                .map(userInfo -> modelMapper.map(userInfo, UserInfoResponse.class))
                .toList();
    }
}
