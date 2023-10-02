package com.blogvista.backend.model.verify_email;

import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VerifyEmailRequest {
    @Email
    private String email;
}
