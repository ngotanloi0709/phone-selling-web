package com.fighting.phonesellingweb.controlller;

import com.fighting.phonesellingweb.model.Brand;
import com.fighting.phonesellingweb.model.Phone;
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

    @GetMapping()
    public String getAdmin(){
        return "admin/index";
    }

    @GetMapping("/product")
    public String getProduct(Model model) {
        model.addAttribute("phones", phoneService.findAllPhones());
        model.addAttribute("brands", brandService.findAllBrands());

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

    @GetMapping("/product/add")
    public String showAddPhoneForm(Model model) {
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
    public String editPhone(@PathVariable Integer id, Model model) {
        Phone phone = phoneService.findPhoneById(id);
        model.addAttribute("phone", phone);
        model.addAttribute("brands", brandService.findAllBrands());

        return "admin/edit_phone";
    }

    @GetMapping("/product/brand/add")
    public String showAddBrandForm(Model model) {
        model.addAttribute("brand", new Brand());
        return "admin/add_brand";
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

    @PostMapping("/product/brand/add")
    public String addBrand(@ModelAttribute Brand brand) {
        brandService.createBrand(brand);
        return "redirect:/admin/product";
    }

    @GetMapping("/product/brand/edit/{id}")
    public String editEmployee(@PathVariable Integer id, Model model) {
        Brand brand = brandService.findBrandById(id);
        model.addAttribute("brand", brand);

        return "admin/edit_brand";
    }

    @PostMapping("/product/brand/edit/{id}")
    public String editBrand(@ModelAttribute Brand brand) {
        brandService.updateBrand(brand);
        return "redirect:/admin/product";
    }

    @PostMapping("/product/brand/delete/{id}")
    public String deleteBrand(@PathVariable Integer id) {
        brandService.deleteBrand(id);
        return "redirect:/admin/product";
    }
}
