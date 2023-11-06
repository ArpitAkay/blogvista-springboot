package com.blogvista.backend.model.user_info;

import com.blogvista.backend.entity.Role;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserInfoResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String profileImageUrl;
    private List<Role> roles = new ArrayList<>();
}
