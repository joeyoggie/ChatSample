package com.elvesapp.chatsample;

public class Constants {
    public static final String PACKAGE_NAME = "com.elvesapp.chatsample";

    //endpoint URLs
    public static final String DOMAIN_URL = "";
    public static final String LOGIN_URL = DOMAIN_URL +  "/mb/v2/sessions";
    public static final String SIGN_UP = DOMAIN_URL + "/mb/v2/users";
    public static final String CHECKOUT_URL = DOMAIN_URL + "/checkout/v3/users";

    //parameters
    public static final String PARAMETER_ERROR = "error";
    public static final String PARAMETER_ERRORS = "errors";

    //user related parameters
    public static final String PARAMETER_ID = "id";
    public static final String PARAMETER_NAME = "name";
    public static final String PARAMETER_FIRST_NAME = "first_name";
    public static final String PARAMETER_LAST_NAME = "last_name";
    public static final String PARAMETER_EMAIL = "email";
    public static final String PARAMETER_PASSWORD = "password";
    public static final String PARAMETER_ACCESS_TOKEN = "token";
    public static final String PARAMETER_FACEBOOK_ACCESS_TOKEN = "access_token";
    public static final String PARAMETER_AUTH_HEADER = "AUTHORIZATION";
    public static final String PARAMETER_WALLET_STRING_VALUE = "wallet_string_value";
    public static final String PARAMETER_LOGIN_EMAIL = "login_email";
    public static final String PARAMETER_CONTENT = "content";
    public static final String PARAMETER_LOGIN_EMAIL_ATTRIBUTES = "login_email_attributes";
    public static final String PARAMETER_USER = "user";

    public static final String PARAMETER_IMAGE_URL = "image_url";

    //notification channel constant, only for api26+
    public static final String CHANNEL_ID = "44";
}
