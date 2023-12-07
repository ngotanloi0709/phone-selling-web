package com.fighting.phonesellingweb.config;
import com.fighting.phonesellingweb.util.CookieUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;

public class SessionFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Cookie[] cookies = ((HttpServletRequest) request).getCookies();

        HttpSession session = ((HttpServletRequest) request).getSession(false);

        if (session == null || session.getAttribute("SPRING_SECURITY_CONTEXT") == null) {
            CookieUtil.clearCookies((HttpServletResponse) response);
        }

        if (cookies != null) {
            boolean hasEmailCookie = Arrays.stream(cookies)
                    .anyMatch(cookie -> cookie.getName().equals("email"));
            boolean hasJSessionIdCookie = Arrays.stream(cookies)
                    .anyMatch(cookie -> cookie.getName().equals("JSESSIONID"));

            if (!hasEmailCookie && hasJSessionIdCookie) {
                CookieUtil.clearCookies((HttpServletResponse) response);
                ((HttpServletResponse) response).sendRedirect("/account/login");

                return;
            }
        }

        chain.doFilter(request, response);
    }
}