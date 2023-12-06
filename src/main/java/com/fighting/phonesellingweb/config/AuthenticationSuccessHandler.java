package com.fighting.phonesellingweb.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // get user details
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // create cookie
        Cookie cookie = new Cookie("email", userDetails.getUsername());
        // set cookie to expire in 7 days
        cookie.setMaxAge(7 * 24 * 60 * 60);
        // cookie is valid for all application path
        cookie.setPath("/");
        // add cookie to response
        response.addCookie(cookie);
        // redirect to home page
        response.sendRedirect("/");

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
