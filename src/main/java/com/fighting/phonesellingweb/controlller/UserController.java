package com.fighting.phonesellingweb.controlller;

import com.fighting.phonesellingweb.dto.ChangePasswordRequest;
import com.fighting.phonesellingweb.dto.ProfileUpdateRequest;
import com.fighting.phonesellingweb.model.User;
import com.fighting.phonesellingweb.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@Controller
@RequestMapping("/account")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @GetMapping("/profile")
    public String getDetailUser(@CookieValue(name = "email", required = false) String email, Model model) {
        if (email == null) {
            return "login";
        }

        User user = userService.findUserByEmail(email);

        if (user != null) {
            model.addAttribute("user", user);

            if (user.getAvatar() != null) {
                String base64Avatar = Base64.getEncoder().encodeToString(user.getAvatar());

                // Gửi dữ liệu base64 đến view
                model.addAttribute("base64Avatar", base64Avatar);
            }

            return "profile";
        }

        return "redirect:/";
    }

    @PostMapping("/profile")
    public String updateUser(@CookieValue(name = "email", required = false) String email,
                             @ModelAttribute ProfileUpdateRequest request) throws IOException {
        if (email == null) {
            return "login";
        }

        User user = userService.findUserByEmail(email);

        if (user != null) {
            if (request.getAvatar().getOriginalFilename() != null && !request.getAvatar().getOriginalFilename().equals("")) {
                String fileName = StringUtils.cleanPath(request.getAvatar().getOriginalFilename());
                if (fileName.contains("..")) {
                    System.out.println("not a a valid file");
                }
                try {
                    user.setAvatar(request.getAvatar().getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            user.setPhone(request.getPhone());
            user.setName(request.getName());
            user.setAddress(request.getAddress());

            userService.updateUser(user);

            return "redirect:/account/profile";
        }

        return "redirect:/";
    }

    @GetMapping("/change-pwd")
    public String changePwd(@CookieValue(name = "email", required = false) String email, @ModelAttribute String newPwd, Model model) {
        if (email == null) {
            return "login";
        }

        User user = userService.findUserByEmail(email);

        if (user != null) {
            model.addAttribute("user", user);

            if (user.getAvatar() != null) {
                String base64Avatar = Base64.getEncoder().encodeToString(user.getAvatar());

                // Gửi dữ liệu base64 đến view
                model.addAttribute("base64Avatar", base64Avatar);
            }

            return "change-password";
        }

        return "redirect:/";
    }

    @PostMapping("/change-pwd")
    public String changePwd(@CookieValue(name = "email", required = false) String email,
                            @ModelAttribute ChangePasswordRequest request, HttpServletResponse response) throws IOException {
        if (email == null) {
            return "login";
        }

        User user = userService.findUserByEmail(email);

        String pwd = passwordEncoder.encode(request.getPassword());

        user.setPassword(pwd);

        userService.updateUser(user);

        Cookie emailC = new Cookie("email", null);
        emailC.setMaxAge(0);
        emailC.setPath("/");
        Cookie jSessionId = new Cookie("JSESSIONID", null);
        jSessionId.setMaxAge(0);
        jSessionId.setPath("/");
        Cookie rememberMe = new Cookie("remember-me", null);
        rememberMe.setMaxAge(0);
        rememberMe.setPath("/");

        response.addCookie(emailC);
        response.addCookie(jSessionId);
        response.addCookie(rememberMe);

        return "redirect:/";
    }
}
