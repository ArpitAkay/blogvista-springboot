package com.blogvista.backend.service;

import com.blogvista.backend.exception.RESTException;
import com.blogvista.backend.model.blog.BlogRequest;
import com.blogvista.backend.model.blog.BlogResponse;
import com.blogvista.backend.model.blog.PaginatedBlogReponse;
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

    List<BlogResponse> getBlogsByEmail(
    ) throws RESTException;

    PaginatedBlogReponse getAllBlogs(
            int pageNo,
            int pageSize
    );

    BlogResponse updateBlogById(
            int blogId,
            BlogRequest blogRequest
    ) throws RESTException;

    String deleteBlogById(
            int blogId
    ) throws RESTException;
}
