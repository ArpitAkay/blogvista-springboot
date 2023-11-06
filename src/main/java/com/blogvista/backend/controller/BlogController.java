package com.blogvista.backend.controller;

import com.blogvista.backend.constant.Endpoint;
import com.blogvista.backend.exception.RESTException;
import com.blogvista.backend.model.blog.BlogResponse;
import com.blogvista.backend.model.blog.PaginatedBlogReponse;
import com.blogvista.backend.service.BlogService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
            @RequestPart(name = "blogData") String blogRequestInString,
            @RequestPart(name = "blogPreviewImage", required = false) MultipartFile multipartFile
    ) throws RESTException, IOException {
        System.out.println("blogRequestInString = " + blogRequestInString);
        System.out.println("multipartFile = " + multipartFile);
        return new ResponseEntity<>(blogService.createBlog(blogRequestInString, multipartFile), HttpStatus.OK);
    }

    @GetMapping(Endpoint.GET_BLOG_BY_ID)
    public ResponseEntity<BlogResponse> getBlogById(
            @RequestParam("blogId") int blogId
    ) throws RESTException {
        return new ResponseEntity<>(blogService.getBlogById(blogId), HttpStatus.OK);
    }

    @GetMapping(Endpoint.GET_BLOGS_BY_EMAIL)
    public ResponseEntity<PaginatedBlogReponse> getBlogsByEmail(
            @RequestParam("pageNo") int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "5", required = false) int pageSize
    ) throws RESTException {
        return new ResponseEntity<>(blogService.getBlogsByEmail(pageNo, pageSize), HttpStatus.OK);
    }

    @GetMapping(Endpoint.GET_ALL_BLOGS)
    public ResponseEntity<PaginatedBlogReponse> getAllBlogs(
            @RequestParam("pageNo") int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "5", required = false) int pageSize
    ) {
        return new ResponseEntity<>(blogService.getAllBlogs(pageNo, pageSize), HttpStatus.OK);
    }

    @PatchMapping(Endpoint.UPDATE_BLOG_BY_ID)
    public ResponseEntity<BlogResponse> updateBlogById(
            @RequestParam("blogId") int blogId,
            @RequestPart(name = "blogData") String blogRequestInString,
            @RequestPart(name = "blogPreviewImage", required = false) MultipartFile multipartFile
    ) throws RESTException, JsonProcessingException {
        return new ResponseEntity<>(blogService
                .updateBlogById(blogId, blogRequestInString, multipartFile), HttpStatus.OK);
    }

    @DeleteMapping(Endpoint.DELETE_BLOG_BY_ID)
    public ResponseEntity<String> deleteBlogById(
            @RequestParam("blogId") int blogId
    ) throws RESTException {
        return new ResponseEntity<>(blogService.deleteBlogById(blogId), HttpStatus.OK);
    }

    @PatchMapping(Endpoint.UPDATE_BLOG_STATUS)
    public ResponseEntity<String> updateBlogStatus(
            @RequestParam("blogId") int blogId,
            @RequestParam("blogStatus") String blogStatus
    ) throws RESTException {
        return new ResponseEntity<>(blogService.updateBlogStatus(blogId, blogStatus), HttpStatus.OK);
    }

    @GetMapping(Endpoint.SEARCH_BLOGS)
    public ResponseEntity<PaginatedBlogReponse> searchBlogs(
            @RequestParam("pageNo") int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "5", required = false) int pageSize,
            @RequestParam("query") String query
    ) {
        return new ResponseEntity<>(blogService.searchBlogs(pageNo, pageSize, query), HttpStatus.OK);
    }
}
