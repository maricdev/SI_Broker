package com.si.broker.utils;

public class Constants {

    public static final String PARAMETERS_BASE_URL = "/api/v1/services/parameters";
    public static final String ENDPOINTS_BASE_URL = "/api/v1/services/endpoints";
    public static final String PROVIDERS_BASE_URL = "/api/v1/providers";
    public static final String SERVICES_BASE_URL = "/api/v1/services";
    public static final String USERS_BASE_URL = "/api/v1/users";
    public static final String ROLES_BASE_URL = "/api/v1/roles";

    public static final String SECRET_KEY = "SISecretKeyForJWT";
    public static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final long EXPIRATION_TIME = 3600000;

}
