package com.blogvista.backend.service_impl;

import com.blogvista.backend.dao.UserInfoUserDetails;
import com.blogvista.backend.entity.Blog;
import com.blogvista.backend.entity.UserInfo;
import com.blogvista.backend.enumeration.BlogStatus;
import com.blogvista.backend.exception.RESTException;
import com.blogvista.backend.model.blog.BlogRequest;
import com.blogvista.backend.model.blog.BlogResponse;
import com.blogvista.backend.model.blog.PaginatedBlogReponse;
import com.blogvista.backend.repository.BlogRepository;
import com.blogvista.backend.repository.UserInfoRepository;
import com.blogvista.backend.service.BlogService;
import com.blogvista.backend.util.S3Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.Predicate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BlogServiceImpl implements BlogService {
    public static final String BLOG_NOT_FOUND_WITH_BLOG = "Blog not found with blogId : ";
    private final BlogRepository blogRepository;
    private final ModelMapper modelMapper;
    private final UserInfoRepository userInfoRepository;
    private final S3Util s3Util;
    @Value("${application.bucket.name}")
    private String bucketName;

    public BlogServiceImpl(
            BlogRepository blogRepository,
            ModelMapper modelMapper,
            S3Util s3Util,
            UserInfoRepository userInfoRepository
    ) {
        this.blogRepository = blogRepository;
        this.modelMapper = modelMapper;
        this.s3Util = s3Util;
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public BlogResponse createBlog(
            String blogRequestInString,
            MultipartFile multipartFile
    ) throws IOException, RESTException {
        ObjectMapper objectMapper = new ObjectMapper();
        BlogRequest blogRequest = objectMapper.readValue(blogRequestInString, BlogRequest.class);

        UserInfoUserDetails userInfoUserDetails = (UserInfoUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        String email = userInfoUserDetails.getUsername();

        UserInfo userInfoOptional = userInfoRepository.findByEmail(email)
                .orElseThrow(() -> new RESTException("User info not found with email " + email));


        Blog blog = modelMapper.map(blogRequest, Blog.class);
        blog.setAuthor(userInfoOptional.getFirstName() + " " + userInfoOptional.getLastName());
        blog.setUserInfo(userInfoOptional);

        if (multipartFile != null) {
            String fileNameWithUUID = s3Util.uploadFileToS3Bucket(multipartFile);
            String imageUrlFromS3 = s3Util.getImageUrlFromS3(fileNameWithUUID);
            blog.setPreviewImageUrl(imageUrlFromS3);
            blog.setPreviewImageName(fileNameWithUUID);
        }

        Blog blogSaved = blogRepository.save(blog);
        return modelMapper.map(blogSaved, BlogResponse.class);
    }

    @Override
    public BlogResponse getBlogById(
            int blogId
    ) throws RESTException {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RESTException(BLOG_NOT_FOUND_WITH_BLOG + blogId));
        return modelMapper.map(blog, BlogResponse.class);
    }

    @Override
    public PaginatedBlogReponse getBlogsByEmail(
            int pageNo,
            int pageSize
    ) throws RESTException {
        PaginatedBlogReponse paginatedBlogReponse = new PaginatedBlogReponse();
        UserInfoUserDetails userInfoUserDetails = (UserInfoUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        String email = userInfoUserDetails.getUsername();

        UserInfo userInfo = userInfoRepository.findByEmail(email)
                .orElseThrow(() -> new RESTException("User info not found with email " + email));

        //find all blogs by email
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        Page<Blog> blogs = blogRepository.findByUserInfo(Sort.by(Sort.Direction.DESC, "createdDate"), userInfo, pageRequest);

        paginatedBlogReponse.setTotalBlogs(blogs.getTotalElements());
        paginatedBlogReponse.setTotalPages(blogs.getTotalPages());
        paginatedBlogReponse.setCurrentPage(blogs.getNumber());
        paginatedBlogReponse.setPageSize(blogs.getSize());
        paginatedBlogReponse.setHasNext(blogs.hasNext());
        paginatedBlogReponse.setHasPrevious(blogs.hasPrevious());

        List<BlogResponse> blogResponseList = blogs
                .stream().map(blog -> modelMapper.map(blog, BlogResponse.class)).toList();
        paginatedBlogReponse.setBlogs(blogResponseList);

        return paginatedBlogReponse;
    }

    @Override
    public PaginatedBlogReponse getAllBlogs(int pageNo, int pageSize) {
        PaginatedBlogReponse paginatedBlogReponse = new PaginatedBlogReponse();

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        Page<Blog> blogs = blogRepository.findAllByStatus(Sort.by(Sort.Direction.DESC, "createdDate"), BlogStatus.CREATED, pageRequest);

        paginatedBlogReponse.setTotalBlogs(blogs.getTotalElements());
        paginatedBlogReponse.setTotalPages(blogs.getTotalPages());
        paginatedBlogReponse.setCurrentPage(blogs.getNumber());
        paginatedBlogReponse.setPageSize(blogs.getSize());
        paginatedBlogReponse.setHasNext(blogs.hasNext());
        paginatedBlogReponse.setHasPrevious(blogs.hasPrevious());

        List<BlogResponse> blogResponseList = blogs
                .stream().map(blog -> modelMapper.map(blog, BlogResponse.class)).toList();
        paginatedBlogReponse.setBlogs(blogResponseList);

        return paginatedBlogReponse;
    }

    @Override
    public BlogResponse updateBlogById(
            int blogId,
            String blogRequestInString,
            MultipartFile multipartFile
    ) throws RESTException, JsonProcessingException {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RESTException(BLOG_NOT_FOUND_WITH_BLOG + blogId));

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> blogData = objectMapper.readValue(blogRequestInString, Map.class);

        blogData.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Blog.class, key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, blog, value);
        });

        Blog blogSaved = blogRepository.save(blog);

        if (multipartFile != null) {
            s3Util.deleteFileInS3Bucket(blog.getPreviewImageName());
            String fileNameWithUUID = s3Util.uploadFileToS3Bucket(multipartFile);
            String imageUrlFromS3 = s3Util.getImageUrlFromS3(fileNameWithUUID);
            blogSaved.setPreviewImageUrl(imageUrlFromS3);
            blogSaved.setPreviewImageName(fileNameWithUUID);
            blogRepository.save(blogSaved);
        }
        return modelMapper.map(blogSaved, BlogResponse.class);
    }

    @Override
    public String deleteBlogById(
            int blogId
    ) throws RESTException {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RESTException(BLOG_NOT_FOUND_WITH_BLOG + blogId));
        if (blog.getPreviewImageName() != null) s3Util.deleteFileInS3Bucket(blog.getPreviewImageName());
        blogRepository.delete(blog);
        return "Blog deleted successfully";
    }

    @Override
    public String updateBlogStatus(
            int blogId,
            String blogStatus
    ) throws RESTException {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RESTException(BLOG_NOT_FOUND_WITH_BLOG + blogId));
        if (blogStatus.equalsIgnoreCase("PUBLISH")) {
            blog.setStatus(BlogStatus.PUBLISHED);
        } else {
            blog.setStatus(BlogStatus.CREATED);
        }
        blogRepository.save(blog);
        return "Blog status updated successfully";
    }

    @Override
    public PaginatedBlogReponse searchBlogs(
            int pageNo,
            int pageSize,
            String searchQuery
    ) {
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);

        Page<Blog> blogs = blogRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                predicates.add(
                        criteriaBuilder.or(
                                criteriaBuilder.like(root.get("title"), "%" + searchQuery + "%"),
                                criteriaBuilder.like(root.get("category"), "%" + searchQuery + "%"),
                                criteriaBuilder.like(root.get("author"), "%" + searchQuery + "%")
                        ));

            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, pageRequest);

        PaginatedBlogReponse paginatedBlogReponse = new PaginatedBlogReponse();
        paginatedBlogReponse.setTotalBlogs(blogs.getTotalElements());
        paginatedBlogReponse.setTotalPages(blogs.getTotalPages());
        paginatedBlogReponse.setCurrentPage(blogs.getNumber());
        paginatedBlogReponse.setPageSize(blogs.getSize());
        paginatedBlogReponse.setHasNext(blogs.hasNext());
        paginatedBlogReponse.setHasPrevious(blogs.hasPrevious());

        List<BlogResponse> blogResponseList = blogs
                .stream().map(blog -> modelMapper.map(blog, BlogResponse.class)).toList();
        paginatedBlogReponse.setBlogs(blogResponseList);

        return paginatedBlogReponse;
    }
}
