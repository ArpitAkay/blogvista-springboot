package com.blogvista.backend.model.category;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryResponse {
    private int categoryId;
    private String title;
}
