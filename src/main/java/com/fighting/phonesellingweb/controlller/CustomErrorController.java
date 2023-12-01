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
    public String getError() {
        return "error";
    }
}
