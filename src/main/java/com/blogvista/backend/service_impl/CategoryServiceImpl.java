package com.blogvista.backend.service_impl;

import com.blogvista.backend.entity.Category;
import com.blogvista.backend.model.category.CategoryResponse;
import com.blogvista.backend.repository.CategoryRepository;
import com.blogvista.backend.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(
            CategoryRepository categoryRepository,
            ModelMapper modelMapper
    ) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
//        String[] categoriesToSave = {
//                "Arts and Culture",
//                "Business and Entrepreneurship",
//                "Education",
//                "Finance and Investing",
//                "Food and Cooking",
//                "Gaming",
//                "Health and Wellness",
//                "History and Historical Events",
//                "Lifestyle",
//                "News and Current Events",
//                "Pets and Animals",
//                "Science",
//                "Sports and Fitness",
//                "Technology",
//                "Travel and Adventure",
//        };
//
//        // sort this string array via alphabetical order
//        Arrays.sort(categoriesToSave);
//
//        Arrays.stream(categoriesToSave).map(category -> {
//            Category categoryToSave = new Category();
//            categoryToSave.setTitle(category);
//            return categoryToSave;
//        }).forEach(categoryRepository::save);
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(category -> modelMapper.map(category, CategoryResponse.class)).toList();
    }
}
