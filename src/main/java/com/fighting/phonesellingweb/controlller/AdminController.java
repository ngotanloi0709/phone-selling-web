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
    public String getAdmin(){
        return "admin/index";
    }

    @GetMapping("/user")
    public String getUser(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("mapUser", new User());

        return "admin/user";
    }

    @GetMapping("/product")
    public String getProduct(Model model) {
        model.addAttribute("phones", phoneService.findAllPhones());
        model.addAttribute("brands", brandService.findAllBrands());
        model.addAttribute("phone", new Phone());
        model.addAttribute("brand", new Brand());

        return "admin/product";
    }

    @GetMapping("/order")
    public String getOrder(){
        return "admin/order";
    }
//
//    Code Section for user
//
    @PostMapping("/user/add")
    public String addUser(@ModelAttribute("mapUser") User mapUser) {
        userService.createUser(mapUser);

        return "redirect:/admin/user";
    }

    @PostMapping("/user/edit")
    public String editUser(@ModelAttribute("mapUser") User mapUser, @RequestParam String email) {
        User existingUser = userService.findUserByEmail(email);
        if (existingUser == null) {
            return "redirect:/admin/user";
        }

        existingUser.setName(mapUser.getName());
        existingUser.setAddress(mapUser.getAddress());
        existingUser.setPhone(mapUser.getPhone());

        userService.updateUser(existingUser);

        return "redirect:/admin/user";
    }

    @PostMapping("/user/delete")
    public String deleteUser(@RequestParam Integer id) {
        userService.deleteUser(id);

        return "redirect:/admin/user";
    }
//
//    Code Section for product(phone)
//
    @PostMapping("/product/add")
    public String addPhone(@ModelAttribute Phone phone) {
        phoneService.createPhone(phone);

        return "redirect:/admin/product";
    }

    @PostMapping("/product/edit")
    public String editPhone(@ModelAttribute Phone phone) {
        phoneService.updatePhone(phone);

        return "redirect:/admin/product";
    }

    @PostMapping("/product/delete")
    public String deletePhone(@RequestParam Integer id) {
        phoneService.deletePhone(id);

        return "redirect:/admin/product";
    }
//
//    Code Section for brand
//
    @PostMapping("/product/brand/add")
    public String addBrand(@ModelAttribute Brand brand) {
        if (brandService.isBrandExists(brand.getName())) {
            return "redirect:/admin/product";
        }

        brandService.createBrand(brand);

        return "redirect:/admin/product";
    }

    @PostMapping("/product/brand/edit")
    public String editBrand(@ModelAttribute Brand brand) {
        if (brandService.isBrandExists(brand.getName())) {
            return "redirect:/admin/product";
        }

        brandService.updateBrand(brand);

        return "redirect:/admin/product";
    }

    @PostMapping("/product/brand/delete")
    public String deleteBrand(@RequestParam Integer id) {
        brandService.deleteBrand(id);
        return "redirect:/admin/product";
    }
}
