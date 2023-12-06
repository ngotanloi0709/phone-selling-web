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

// hàm này ông làm j á để xử lý session cho riêng biệt mỗi lần khởi động nó tự out ra á
// cẩn thận nha, seesion trong java cũng lằng ngoàng á cái nào á ông
// ý là session ông sài thì như cẩn thận, tại java t cấu hình session cũng lằng ngoằng. kkk
// chỗ getdata của ông đâu
