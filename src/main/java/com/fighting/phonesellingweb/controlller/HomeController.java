package com.fighting.phonesellingweb.controlller;

import com.fighting.phonesellingweb.model.Brand;
import com.fighting.phonesellingweb.model.Phone;
import com.fighting.phonesellingweb.model.User;
import com.fighting.phonesellingweb.service.BrandService;
import com.fighting.phonesellingweb.service.PhoneService;
import com.fighting.phonesellingweb.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
                       @RequestParam(defaultValue = "3") int size,
                       @RequestParam(required = false) Integer brandId,
                       Model model) {
        if (email != null) {
            User user = userService.findUserByEmail(email);
            model.addAttribute("user", user);
        }

        List<Phone> phones;
        if (brandId != null) {
            phones = phoneService.findPhonesByBrand(brandId);
        } else {
            phones = phoneService.getAllPhones();
        }
        model.addAttribute("phones", phones);
        model.addAttribute("brands", brandService.findAllBrands());
        model.addAttribute("brandId", brandId);

        // Add randomPhonePage to the model
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Phone> randomPhonePage = phoneService.findRandomPhones(pageable);
        model.addAttribute("randomPhonePage", randomPhonePage);

        return "index";
    }

    @GetMapping("/brand/{brandId}/phones")
    @ResponseBody
    public List<Phone> getPhonesByBrand(@PathVariable int brandId) {
        return phoneService.findPhonesByBrand(brandId);
    }



}