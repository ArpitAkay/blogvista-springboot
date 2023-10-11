package com.blogvista.backend.entity;

import com.blogvista.backend.enumeration.BlogStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Blog extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int blogId;
    private String title;
    private String author;
    private String category;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;
    private String previewImageUrl;
    private String previewImageName;
    private BlogStatus status;

    @ManyToOne
    @JoinColumn(
            name = "email",
            referencedColumnName = "email"
    )
    private UserInfo userInfo;
}
