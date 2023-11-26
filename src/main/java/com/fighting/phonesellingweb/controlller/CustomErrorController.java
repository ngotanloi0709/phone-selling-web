package com.fighting.phonesellingweb.controlller;

import com.fighting.phonesellingweb.model.User;
import com.fighting.phonesellingweb.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
public class CustomErrorController implements ErrorController {
    private UserService userService;

    @RequestMapping("/error")
    public String getError(@CookieValue(name="email", required = false) String email, Model model) {
        if (email != null) {
            User user = userService.findUserByEmail(email);
            model.addAttribute("user", user);
        }

        return "error";
    }
}
