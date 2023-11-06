package com.blogvista.backend.service;

import com.blogvista.backend.exception.RESTException;
import com.blogvista.backend.model.blog.BlogResponse;
import com.blogvista.backend.model.blog.PaginatedBlogReponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BlogService {
    BlogResponse createBlog(
            String blogRequestInString,
            MultipartFile multipartFile
    ) throws IOException, RESTException;

    BlogResponse getBlogById(
            int blogId
    ) throws RESTException;

    PaginatedBlogReponse getBlogsByEmail(
            int pageNo,
            int pageSize
    ) throws RESTException;

    PaginatedBlogReponse getAllBlogs(
            int pageNo,
            int pageSize
    );

    BlogResponse updateBlogById(
            int blogId,
            String blogRequestInString,
            MultipartFile multipartFile
    ) throws RESTException, JsonProcessingException;

    String deleteBlogById(
            int blogId
    ) throws RESTException;

    String updateBlogStatus(
            int blogId,
            String blogStatus
    ) throws RESTException;

    PaginatedBlogReponse searchBlogs(
            int pageNo,
            int pageSize,
            String query
    );
}
