package com.blogvista.backend.model.blog;

import com.blogvista.backend.enumeration.BlogStatus;
import com.blogvista.backend.model.user_info.UserInfoResponse;
import jakarta.persistence.Lob;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BlogResponse {
    private String blogId;
    private String title;
    private String author;
    private String category;
    @Lob
    private String content;
    private String previewImageUrl;
    private BlogStatus status;
    private Date createdDate;
    private UserInfoResponse userInfo;
}
