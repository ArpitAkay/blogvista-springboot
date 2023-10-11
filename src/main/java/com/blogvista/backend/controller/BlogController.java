package com.blogvista.backend.controller;

import com.blogvista.backend.constant.Endpoint;
import com.blogvista.backend.exception.RESTException;
import com.blogvista.backend.model.blog.BlogRequest;
import com.blogvista.backend.model.blog.BlogResponse;
import com.blogvista.backend.model.blog.PaginatedBlogReponse;
import com.blogvista.backend.service.BlogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(Endpoint.BLOG_ENDPOINT)
public class BlogController {
    private final BlogService blogService;

    public BlogController(
            BlogService blogService
    ) {
        this.blogService = blogService;
    }

    @PostMapping(Endpoint.CREATE_BLOG)
    public ResponseEntity<BlogResponse> createBlog(
            @Valid @RequestPart(name = "blogData") String blogRequestInString,
            @RequestPart(name = "blogPreviewImage", required = false) MultipartFile multipartFile
    ) throws RESTException, IOException {
        return new ResponseEntity<>(blogService.createBlog(blogRequestInString, multipartFile), HttpStatus.OK);
    }

    @GetMapping(Endpoint.GET_BLOG_BY_ID)
    public ResponseEntity<BlogResponse> getBlogById(
            @RequestParam("blogId") int blogId
    ) throws RESTException {
        return new ResponseEntity<>(blogService.getBlogById(blogId), HttpStatus.OK);
    }

    @GetMapping(Endpoint.GET_BLOGS_BY_EMAIL)
    public ResponseEntity<List<BlogResponse>> getBlogsByEmail() throws RESTException {
        return new ResponseEntity<>(blogService.getBlogsByEmail(), HttpStatus.OK);
    }

    @GetMapping(Endpoint.GET_ALL_BLOGS)
    public ResponseEntity<PaginatedBlogReponse> getAllBlogs(
            @RequestParam("pageNo") int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "5", required = false) int pageSize
    ) {
        return new ResponseEntity<>(blogService.getAllBlogs(pageNo, pageSize), HttpStatus.OK);
    }

    @PutMapping(Endpoint.UPDATE_BLOG_BY_ID)
    public ResponseEntity<BlogResponse> updateBlogById(
            @RequestParam("blogId") int blogId,
            @Valid @RequestBody BlogRequest blogRequest
    ) throws RESTException {
        return new ResponseEntity<>(blogService.updateBlogById(blogId, blogRequest), HttpStatus.OK);
    }

    @DeleteMapping(Endpoint.DELETE_BLOG_BY_ID)
    public ResponseEntity<String> deleteBlogById(
            @RequestParam("blogId") int blogId
    ) throws RESTException {
        return new ResponseEntity<>(blogService.deleteBlogById(blogId), HttpStatus.OK);
    }
}
