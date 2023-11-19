package com.fighting.phonesellingweb.controlller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class HomeController {
    @GetMapping({"/", "/home"})
    public String getHome(){
        return "home";
    }
}
