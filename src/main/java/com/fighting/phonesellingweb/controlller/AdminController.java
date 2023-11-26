package com.fighting.phonesellingweb.controlller;

import com.fighting.phonesellingweb.model.Brand;
import com.fighting.phonesellingweb.model.Phone;
import com.fighting.phonesellingweb.model.User;
import com.fighting.phonesellingweb.service.BrandService;
import com.fighting.phonesellingweb.service.PhoneService;
import com.fighting.phonesellingweb.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
    private UserService userService;
    private PhoneService phoneService;
    private BrandService brandService;

    @GetMapping({"", "/"})
    public String getAdmin(@CookieValue(name="email", required = false) String email, Model model){
        if (email != null) {
            User user = userService.findUserByEmail(email);
            model.addAttribute("user", user);
        }

        return "admin/index";
    }

    @GetMapping("/user")
    public String getUser(@CookieValue(name="email", required = false) String email, Model model) {
        if (email != null) {
            User user = userService.findUserByEmail(email);
            model.addAttribute("user", user);
        }

        model.addAttribute("users", userService.findAllUsers());

        return "admin/user";
    }

    @GetMapping("/product")
    public String getProduct(@CookieValue(name="email", required = false) String email, Model model) {
        if (email != null) {
            User user = userService.findUserByEmail(email);
            model.addAttribute("user", user);
        }

        model.addAttribute("phones", phoneService.findAllPhones());
        model.addAttribute("brands", brandService.findAllBrands());

        return "admin/product";
    }

    @GetMapping("/order")
    public String getOrder(@CookieValue(name="email", required = false) String email, Model model){
        if (email != null) {
            User user = userService.findUserByEmail(email);
            model.addAttribute("user", user);
        }

        return "admin/order";
    }
//
//    Code Section for user
//
    @GetMapping("/user/add")
    public String showAddUserForm(@CookieValue(name="email", required = false) String email, Model model) {
        if (email != null) {
            User user = userService.findUserByEmail(email);
            model.addAttribute("user", user);
        }

        model.addAttribute("userMapping", new User());

        return "admin/add_user";
    }

    @PostMapping("/user/add")
    public String addUser(@ModelAttribute User userMapping) {
        userService.createUser(userMapping);

        return "redirect:/admin/user";
    }

    @GetMapping("/user/edit/{id}")
    public String editUser(@CookieValue(name="email", required = false) String email, @PathVariable Integer id, Model model) {
        if (email != null) {
            User user = userService.findUserByEmail(email);
            model.addAttribute("user", user);
        }

        model.addAttribute("userMapping", userService.findUserById(id));

        return "admin/edit_user";
    }

    @PostMapping("/user/edit/{id}")
    public String editUser(@ModelAttribute User userMapping) {
        if (userService.isUserExists(userMapping.getEmail())) {
            return "redirect:/admin/user";
        }

        userService.updateUser(userMapping);

        return "redirect:/admin/user";
    }

    @PostMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);

        return "redirect:/admin/user";
    }
//
//    Code Section for product(phone)
//
    @GetMapping("/product/add")
    public String showAddPhoneForm(@CookieValue(name="email", required = false) String email, Model model) {
        if (email != null) {
            User user = userService.findUserByEmail(email);
            model.addAttribute("user", user);
        }

        model.addAttribute("phone", new Phone());
        model.addAttribute("brands", brandService.findAllBrands());

        return "admin/add_phone";
    }

    @PostMapping("/product/add")
    public String addPhone(@ModelAttribute Phone phone) {
        phoneService.createPhone(phone);

        return "redirect:/admin/product";
    }

    @GetMapping("/product/edit/{id}")
    public String editPhone(@CookieValue(name="email", required = false) String email, @PathVariable Integer id, Model model) {
        if (email != null) {
            User user = userService.findUserByEmail(email);
            model.addAttribute("user", user);
        }

        model.addAttribute("phone", phoneService.findPhoneById(id));
        model.addAttribute("brands", brandService.findAllBrands());

        return "admin/edit_phone";
    }

    @PostMapping("/product/edit/{id}")
    public String editPhone(@ModelAttribute Phone phone) {
        phoneService.updatePhone(phone);

        return "redirect:/admin/product";
    }

    @PostMapping("/product/delete/{id}")
    public String deletePhone(@PathVariable Integer id) {
        phoneService.deletePhone(id);

        return "redirect:/admin/product";
    }
//
//    Code Section for brand
//
    @GetMapping("/product/brand/add")
    public String showAddBrandForm(@CookieValue(name="email", required = false) String email, Model model) {
        if (email != null) {
            User user = userService.findUserByEmail(email);
            model.addAttribute("user", user);
        }

        model.addAttribute("brand", new Brand());
        return "admin/add_brand";
    }

    @PostMapping("/product/brand/add")
    public String addBrand(@ModelAttribute Brand brand) {
        if (brandService.isBrandExists(brand.getName())) {
            return "redirect:/admin/product";
        }

        brandService.createBrand(brand);

        return "redirect:/admin/product";
    }

    @GetMapping("/product/brand/edit/{id}")
    public String editBrand(@CookieValue(name="email", required = false) String email, @PathVariable Integer id, Model model) {
        if (email != null) {
            User user = userService.findUserByEmail(email);
            model.addAttribute("user", user);
        }

        model.addAttribute("brand", brandService.findBrandById(id));

        return "admin/edit_brand";
    }

    @PostMapping("/product/brand/edit/{id}")
    public String editBrand(@ModelAttribute Brand brand) {
        if (brandService.isBrandExists(brand.getName())) {
            return "redirect:/admin/product";
        }

        brandService.updateBrand(brand);

        return "redirect:/admin/product";
    }

    @PostMapping("/product/brand/delete/{id}")
    public String deleteBrand(@PathVariable Integer id) {
        brandService.deleteBrand(id);
        return "redirect:/admin/product";
    }
}
