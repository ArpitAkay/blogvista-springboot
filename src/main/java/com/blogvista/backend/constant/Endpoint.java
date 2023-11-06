package com.blogvista.backend.constant;

public class Endpoint {
    private Endpoint() {
        super();
    }

    public static final String AUTHENTICATION_ENDPOINT = "/api/v1/auth";
    public static final String LOGIN = "/login";
    public static final String SIGN_UP = "/signup";
    public static final String GOOGLE_SIGN_UP = "/google/signup";
    public static final String SEND_EMAIL_VERIFICATION_MAIL = "/send/email/mail";
    public static final String SEND_FORGET_PASSWORD_MAIL = "/send/forgetPassword/mail";
    public static final String VERIFY_EMAIL_TOKEN = "/verify/email/token";
    public static final String VERIFY_FORGET_PASSWORD_TOKEN = "/verify/forgetPassword/token";
    public static final String FORGET_PASSWORD = "/forgetPassword";
    public static final String GENERATE_ACCESS_TOKEN_VIA_REFRESH_TOKEN = "/generate/accessToken";
    public static final String SEARCH_BLOGS = "/search/blogs";

    public static final String USER_INFO_ENDPOINT = "/api/v1/user";
    public static final String GET_USER_INFO = "/get";
    public static final String GET_ALL_USER_INFO = "/get/all";
    public static final String UPDATE_USER_INFO = "/update";


    public static final String BLOG_ENDPOINT = "/api/v1/blog";
    public static final String CREATE_BLOG = "/create";
    public static final String GET_BLOG_BY_ID = "/get";
    public static final String GET_BLOGS_BY_EMAIL = "/get/byEmail";
    public static final String GET_ALL_BLOGS = "/get/all";
    public static final String UPDATE_BLOG_BY_ID = "/update";
    public static final String DELETE_BLOG_BY_ID = "/delete";
    public static final String UPDATE_BLOG_STATUS = "/update/status";


    public static final String CATEGORY_ENDPOINT = "/api/v1/category";
    public static final String GET_ALL_CATEGORIES = "/get/all";
}