package com.fighting.phonesellingweb.controlller;

import com.fighting.phonesellingweb.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/account")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping("/login")
    public String getLogin(){
        return "login";
    }

    @GetMapping("/register")
    public String getRegister(){
        return "register";
    }

    @PostMapping("/register")
    public String postRegister(@RequestParam Map<String, String> account){
        try {
            String email = account.get("email");
            String password = account.get("password");
            String name = account.get("name");
            String phone = account.get("phone");

            userService.register(email, password, name, phone);

            return "redirect:/account/login";
        } catch (Exception e) {
            e.printStackTrace();

            return "redirect:/account/register";
        }

    }
}
