package com.fighting.phonesellingweb.controlller;

import com.fighting.phonesellingweb.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/account")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping("/login")
    public String getLogin(@CookieValue(name="email", required = false) String email){
        if (email != null) {
            return "redirect:/";
        }

        return "login";
    }

    @GetMapping("/register")
    public String getRegister(@CookieValue(name="email", required = false) String email){
        if (email != null) {
            return "redirect:/";
        }

        return "register";
    }

    @PostMapping("/register")
    public String postRegister(@RequestParam Map<String, String> account, Model model) {
        try {
            String email = account.get("email");
            String password = account.get("password");
            String repeatPassword = account.get("repeat-password");
            String name = account.get("name");
            String phone = account.get("phone");

            if (!password.equals(repeatPassword)) {
                model.addAttribute("error", "Mật khẩu không khớp");

                return "register";
            }

            if (userService.isUserExists(email)) {
                model.addAttribute("error", "Email đã tồn tại");

                return "register";
            }

            userService.register(email, password, name, phone);

            return "redirect:/account/login";
        } catch (Exception e) {
            e.printStackTrace();

            return "register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response, HttpSession session) {
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

        return "redirect:/";
    }
}
