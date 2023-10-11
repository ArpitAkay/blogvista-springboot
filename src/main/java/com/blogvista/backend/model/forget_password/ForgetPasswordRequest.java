package com.blogvista.backend.model.forget_password;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ForgetPasswordRequest {
    private String verificationToken;
    private String newPassword;
}
