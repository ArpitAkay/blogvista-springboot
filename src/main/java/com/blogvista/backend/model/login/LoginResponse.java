package com.blogvista.backend.model.login;

import com.blogvista.backend.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String name;
    private String email;
    private String accessToken;
    private String refreshToken;
    private List<Role> roles;
}
