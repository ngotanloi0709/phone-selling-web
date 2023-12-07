package com.fighting.phonesellingweb.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;

public class CookieUtil {
    public static void createCookie(String name, String value, HttpServletResponse response) {
        clearCookie(name, response);
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(30 * 60); // Set the max age to 30 minutes
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static void clearCookies(HttpServletResponse response) {
        clearCookie("email", response);
        clearCookie("JSESSIONID", response);
        clearCookie("remember-me", response);
    }

    public static void clearCookie(String name, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> name.equals(cookie.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }
        return null;
    }
}