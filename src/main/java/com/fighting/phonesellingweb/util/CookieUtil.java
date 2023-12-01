package com.fighting.phonesellingweb.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {
    public static void clearCookies(HttpServletResponse response) {
        Cookie email = new Cookie("email", null);
        email.setMaxAge(0);
        email.setPath("/");
        Cookie jSessionId = new Cookie("JSESSIONID", null);
        jSessionId.setMaxAge(0);
        jSessionId.setPath("/");
        Cookie rememberMe = new Cookie("remember-me", null);
        rememberMe.setMaxAge(0);
        rememberMe.setPath("/");

        response.addCookie(email);
        response.addCookie(jSessionId);
        response.addCookie(rememberMe);
    }
}
