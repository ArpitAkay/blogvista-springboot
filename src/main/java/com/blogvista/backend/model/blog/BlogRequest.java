package com.blogvista.backend.model.blog;

import com.blogvista.backend.enumeration.BlogStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BlogRequest {
    @NotEmpty
    private String title;
    @NotEmpty
    private String category;
    @Lob
    @NotEmpty
    private String content;
    private BlogStatus status;
}
