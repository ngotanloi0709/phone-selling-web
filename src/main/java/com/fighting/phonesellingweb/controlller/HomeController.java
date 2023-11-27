package com.fighting.phonesellingweb.controlller;

import com.fighting.phonesellingweb.model.Phone;
import com.fighting.phonesellingweb.model.User;
import com.fighting.phonesellingweb.service.BrandService;
import com.fighting.phonesellingweb.service.PhoneService;
import com.fighting.phonesellingweb.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class HomeController {
    private final PhoneService phoneService;
    private final BrandService brandService;

    private final UserService userService;

    public HomeController(PhoneService phoneService, BrandService brandService, UserService userService) {
        this.phoneService = phoneService;
        this.brandService = brandService;
        this.userService = userService;
    }

    @GetMapping({"", "/", "/home"})
    public String home(@CookieValue(name="email", required = false) String email,
                       @RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "3") int size, Model model) {
        if (email != null) {
            User user = userService.findUserByEmail(email);
            model.addAttribute("user", user);
        }

        Page<Phone> randomPhonePage = phoneService.findRandomPhones(page, size);
        model.addAttribute("randomPhonePage", randomPhonePage);
        model.addAttribute("brands", brandService.findAllBrands());

        return "index";
    }


}