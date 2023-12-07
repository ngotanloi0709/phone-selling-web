package com.fighting.phonesellingweb.controlller;

import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
public class CustomErrorController implements ErrorController {
    @RequestMapping("/error")
    public String getError() {
        return "error";
    }
}
