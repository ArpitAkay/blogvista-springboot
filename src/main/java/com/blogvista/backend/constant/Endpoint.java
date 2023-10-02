package com.blogvista.backend.constant;

public class Endpoint {
    private Endpoint() {
        super();
    }

    public static final String AUTHENTICATION_ENDPOINT = "/api/v1/auth";
    public static final String LOGIN = "/login";
    public static final String SIGN_UP = "/signup";
    public static final String GOOGLE_SIGN_UP = "/google/signup";
    public static final String VERIFY_EMAIL = "/verify/email";
    public static final String FORGET_PASSWORD = "/forgetPassword";
    public static final String USER_INFO_ENDPOINT = "/api/v1/user";
    public static final String GET_USER_INFO_ENDPOINT = "/get";
    public static final String GET_ALL_USER_INFO_ENDPOINT = "/get/all";
    public static final String BLOG_ENDPOINT = "/api/v1/blog";
}