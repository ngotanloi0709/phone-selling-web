package com.fighting.phonesellingweb.controlller;

import com.fighting.phonesellingweb.model.User;
import com.fighting.phonesellingweb.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.ui.Model;
import java.util.Base64;

@ControllerAdvice
@AllArgsConstructor
public class GlobalControllerAdvice {
    private final UserService userService;

    @ModelAttribute
    public void addUserAttributes(@CookieValue(name="email", required = false) String email, Model model) {
        if (email != null) {
            User user = userService.findUserByEmail(email);
            model.addAttribute("user", user);
            if (user.getAvatar() != null) {
                model.addAttribute("base64Avatar", Base64.getEncoder().encodeToString(user.getAvatar()));
            }
        }
    }
}