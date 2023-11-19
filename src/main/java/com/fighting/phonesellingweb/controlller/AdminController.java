package com.fighting.phonesellingweb.controlller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
    @GetMapping()
    public String getAdmin(){
        return "admin/index";
    }

    @GetMapping("/product")
    public String getProduct(){
        return "admin/product";
    }

    @GetMapping("/order")
    public String getOrder(){
        return "admin/order";
    }

    @GetMapping("/user")
    public String getUser(){
        return "admin/user";
    }

    @PostMapping("/product")
    public String postProduct(){
        return "admin/product";
    }
}
