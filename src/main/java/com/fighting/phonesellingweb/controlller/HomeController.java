package com.fighting.phonesellingweb.controlller;

import com.fighting.phonesellingweb.model.User;
import com.fighting.phonesellingweb.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class HomeController {
    private UserService userService;

    @GetMapping({"", "/", "/home"})
    public String getHome(@CookieValue(name="email", required = false) String email, Model model) {
        if (email != null) {
            User user = userService.findUserByEmail(email);
            model.addAttribute("user", user);
        }

        return "index";
    }
}
