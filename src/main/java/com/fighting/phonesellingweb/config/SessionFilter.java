package com.fighting.phonesellingweb.config;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

public class SessionFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("SessionFilter init");
    }

    @Override
    public void destroy() {
        System.out.println("SessionFilter destroy");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Cookie[] cookies = ((HttpServletRequest) request).getCookies();

        if (cookies != null) {
            boolean hasEmailCookie = Arrays.stream(cookies)
                    .anyMatch(cookie -> cookie.getName().equals("email"));
            boolean hasJSessionIdCookie = Arrays.stream(cookies)
                    .anyMatch(cookie -> cookie.getName().equals("JSESSIONID"));

            if (!hasEmailCookie && hasJSessionIdCookie) {
                Cookie email = new Cookie("email", null);
                email.setMaxAge(0);
                email.setPath("/");
                Cookie jSessionId = new Cookie("JSESSIONID", null);
                jSessionId.setMaxAge(0);
                jSessionId.setPath("/");
                Cookie rememberMe = new Cookie("remember-me", null);
                rememberMe.setMaxAge(0);
                rememberMe.setPath("/");

                ((HttpServletResponse) response).addCookie(email);
                ((HttpServletResponse) response).addCookie(jSessionId);
                ((HttpServletResponse) response).addCookie(rememberMe);

                ((HttpServletResponse) response).sendRedirect("/account/login");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}