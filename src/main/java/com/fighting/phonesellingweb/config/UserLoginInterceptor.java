package com.fighting.phonesellingweb.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class UserLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Kiểm tra cookie "email"
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("email".equals(cookie.getName()) && cookie.getValue() != null) {
                    // Chuyển hướng nếu người dùng đã đăng nhập
                    response.sendRedirect(request.getContextPath() + "/");
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // Có thể thêm logic sau khi controller xử lý

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Có thể thêm logic sau khi request hoàn thành
    }
}
