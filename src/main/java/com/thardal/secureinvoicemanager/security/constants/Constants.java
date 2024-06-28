package com.thardal.secureinvoicemanager.security.constants;

public class Constants {
    // Security
    public static final String[] PUBLIC_URLS = {"/api/user/login/**", "/api/user/verify/**", "/api/user/image/**", "/api/user/register/**", "/api/user/resetpassword/**"};
    public static final String[] PUBLIC_ROUTES = {"/api/user/login/**", "/api/user/verify/**", "/api/user/refresh/token", "/api/user/image", "/api/user/resetpassword/**"};
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HTTP_OPTIONS_METHOD = "OPTIONS";
    public static final String AUTHORITIES = "authorities";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    public static final String CUSTOM_MANAGEMENT_SERVICE = "CUSTOM_MANAGEMENT_SERVICE";
    public static final String GET_SECURE = "GET_SECURE";
    public static final long ACCESS_TOKEN_EXPIRATION_TIME = 30_000_000;
    public static final long REFRESH_TOKEN_EXPIRATION_TIME = 432_000_000;
}
