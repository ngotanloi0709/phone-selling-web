package com.fighting.phonesellingweb.controlller;

import com.fighting.phonesellingweb.model.Phone;
import com.fighting.phonesellingweb.model.Sale;
import com.fighting.phonesellingweb.model.User;
import com.fighting.phonesellingweb.service.BrandService;
import com.fighting.phonesellingweb.service.PhoneService;
import com.fighting.phonesellingweb.service.SaleService;
import com.fighting.phonesellingweb.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class HomeController {
    private final PhoneService phoneService;
    private final BrandService brandService;

    private final UserService userService;

    private final SaleService saleService;

    public HomeController(PhoneService phoneService, BrandService brandService, UserService userService, SaleService saleService) {
        this.phoneService = phoneService;
        this.brandService = brandService;
        this.userService = userService;
        this.saleService = saleService;
    }

    @GetMapping({"", "/", "/home"})
    public String home(@CookieValue(name="email", required = false) String email,
                       @RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "6") int size,
                       @RequestParam(required = false) Integer brandId,
                       Model model) {
        if (email != null) {
            User user = userService.findUserByEmail(email);
            model.addAttribute("user", user);
            if (user.getAvatar() != null) {
                model.addAttribute("base64Avatar", Base64.getEncoder().encodeToString(user.getAvatar()));
            }
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Phone> phones;
        if (brandId != null) {
            phones = phoneService.findPhonesByBrand(brandId, pageable);
        } else {
            phones = phoneService.getAllPhones(pageable);
        }
        model.addAttribute("phones", phones.getContent());
        model.addAttribute("brands", brandService.findAllBrands());
        model.addAttribute("brandId", brandId);

        // Fetch best sellers
        Page<Phone> bestSellingPhones = saleService.findBestSellingPhones(pageable);
        model.addAttribute("bestSellers", bestSellingPhones.getContent());

        // Fetch random products
        Page<Phone> randomPhones = phoneService.findRandomPhones(pageable);
        model.addAttribute("randomPhones", randomPhones.getContent());

        // Fetch products for product list
        Page<Phone> productList = phoneService.getAllPhones(pageable);
        model.addAttribute("productList", productList.getContent());

        return "index";
    }

//    @GetMapping("/brand/{brandId}/phones")
//    @ResponseBody
//    public List<Phone> getPhonesByBrand(@PathVariable int brandId) {
//        return phoneService.findPhonesByBrand(brandId);
//    }



    // HomeController.java

    @GetMapping("/best-sellers")
    public String bestSellers(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Phone> bestSellingPhones = saleService.findBestSellingPhones(pageable);
        model.addAttribute("bestSellers", bestSellingPhones);
        return "best_sellers";
    }





    @GetMapping("/brand")
    public String viewMoreBrandProducts(@CookieValue(name="email", required = false) String email,
                                        @RequestParam(required = false, defaultValue = "1") Integer brandId,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "9") int size,
                                        Model model) {
        if (email != null) {
            User user = userService.findUserByEmail(email);
            model.addAttribute("user", user);
            if (user.getAvatar() != null) {
                model.addAttribute("base64Avatar", Base64.getEncoder().encodeToString(user.getAvatar()));
            }
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Phone> brandPhones = phoneService.findPhonesByBrand(brandId, pageable);
        model.addAttribute("brandPhones", brandPhones);
        model.addAttribute("brands", brandService.findAllBrands());
        model.addAttribute("brandId", brandId);

        // Calculate total pages
        long totalProducts = phoneService.countProductsByBrand(brandId);
        int totalPages = (int) Math.ceil((double) totalProducts / 9);
        model.addAttribute("totalPages", totalPages);

        return "brand_products";
    }


    // src/main/java/com/fighting/phonesellingweb/controlller/HomeController.java

    @GetMapping("/random-products")
    public String randomProducts(@CookieValue(name="email", required = false) String email,
                                 @RequestParam(defaultValue = "0") int page, Model model,
                                 @RequestParam(required = false, defaultValue = "1") Integer brandId) {
        if (email != null) {
            User user = userService.findUserByEmail(email);
            model.addAttribute("user", user);
            if (user.getAvatar() != null) {
                model.addAttribute("base64Avatar", Base64.getEncoder().encodeToString(user.getAvatar()));
            }
        }

        int productsPerPage = 9;
        Pageable pageable = PageRequest.of(page, productsPerPage);
        Page<Phone> randomPhones = phoneService.findRandomPhones(pageable);
        model.addAttribute("randomPhones", randomPhones);


        // Calculate total pages
        long totalProducts = phoneService.countProducts();
        int totalPages = (int) Math.ceil((double) totalProducts / productsPerPage);
        model.addAttribute("totalPages", totalPages);


        return "random_products";
    }

    @GetMapping("/product-list")
    public String productList(@CookieValue(name="email", required = false) String email,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(required = false, defaultValue = "define") String sort,
                              Model model) {

        if (email != null) {
            User user = userService.findUserByEmail(email);
            model.addAttribute("user", user);
            if (user.getAvatar() != null) {
                model.addAttribute("base64Avatar", Base64.getEncoder().encodeToString(user.getAvatar()));
            }
        }
        int productsPerPage = 9; // Define productsPerPage

        // Check if sort is null or empty, if so, set it to a default value
        if (sort == null || sort.isEmpty()) {
            sort = "define"; // Default sorting
        }

        Sort sorting = Sort.by("name"); // Default sorting
        if (sort.equals("high-to-low")) {
            sorting = Sort.by("price").descending();
        } else if (sort.equals("low-to-high")) {
            sorting = Sort.by("price").ascending();
        } else if (sort.equals("new-product")) {
            sorting = Sort.by("id").descending();
        }
        model.addAttribute("sort", sort);

        Pageable pageable = PageRequest.of(page, productsPerPage, sorting);
        Page<Phone> productList = phoneService.getAllPhones_ProductList(sort, pageable);
        model.addAttribute("productList", productList); // Pass the Page object, not its content

        // Calculate total pages
        long totalProducts = phoneService.countProducts();
        int totalPages = (int) Math.ceil((double) totalProducts / productsPerPage);
        model.addAttribute("totalPages", totalPages);

        return "product_list";
    }




}