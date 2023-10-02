package com.blogvista.backend.dao;

import com.blogvista.backend.entity.UserInfo;
import com.blogvista.backend.exception.RESTException;
import com.blogvista.backend.repository.UserInfoRepository;
import lombok.SneakyThrows;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserInfoUserDetailsService implements UserDetailsService {

    private final UserInfoRepository userInfoRepository;

    public UserInfoUserDetailsService(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserInfo> userInfo = userInfoRepository.findByEmail(email);

        if(userInfo.isPresent()) {
            return new UserInfoUserDetails(userInfo.get());
        }
        else {
            throw new RESTException("Please enter correct email");
        }
    }
}
