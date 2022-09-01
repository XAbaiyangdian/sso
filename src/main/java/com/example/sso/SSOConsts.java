package com.example.sso;

public class SSOConsts {
    public static final class ParamName {
        public static String redirect = "redirect";
        public static String ticket = "ticket";
        public static String back = "back";
        public static String ssoLogoutCall = "ssoLogoutCall";

        public ParamName() {
        }
    }

    public static final class Api {
        public static String ssoAuth = "/sso/auth";
        public static String ssoCheckTicket = "/sso/checkTicket";
        public static String ssoLogout = "/sso/logout";
        public static String ssoPushUser = "/sso/pushUser";
        public static String ssoUserInfo = "/sso/userInfo";

        public Api() {
        }
    }
    public static int TOKEN_TIMEOUT = 10 * 1000;
}
