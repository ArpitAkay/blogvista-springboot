package com.blogvista.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID blogId;
    private String title;
    @Lob
    private String content;
    private String author;
    private String category;
    private String tags;
    private String imageUrl;
    private String videoUrl;
    private String audioUrl;
    private String status;
}
