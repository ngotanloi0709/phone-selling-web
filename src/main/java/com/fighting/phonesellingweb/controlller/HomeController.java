package com.fighting.phonesellingweb.controlller;

import com.fighting.phonesellingweb.model.*;
import com.fighting.phonesellingweb.repository.ProductViewHistoryRepository;
import com.fighting.phonesellingweb.service.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class HomeController {
    private PhoneService phoneService;
    private BrandService brandService;
    private UserService userService;
    private SaleService saleService;
    private ProductViewHistoryService productViewHistoryService;
    private FavoriteService favoriteService;
    private CartService cartService;

    @GetMapping({"", "/", "/home"})
    public String home(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "6") int size,
                       @RequestParam(required = false) Integer brandId,
                       Model model) {
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
    public String viewMoreBrandProducts(@RequestParam(required = false, defaultValue = "1") Integer brandId,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "9") int size,
                                        Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Phone> brandPhones = phoneService.findPhonesByBrand(brandId, pageable);
        model.addAttribute("brandPhones", brandPhones);
        model.addAttribute("brands", brandService.findAllBrands());
        model.addAttribute("brandId", brandId);

        // Calculate total pages
        long totalProducts = phoneService.countProductsByBrand(brandId);
        int totalPages = totalProducts > 0 ? (int) Math.ceil((double) totalProducts / 9) : 0;

        model.addAttribute("totalPages", totalPages);

        return "brand_products";
    }

    @GetMapping("/random-products")
    public String randomProducts(@RequestParam(defaultValue = "0") int page, Model model,
                                 @RequestParam(required = false, defaultValue = "1") Integer brandId) {
        int productsPerPage = 9;
        page = Math.max(0, page);
        Pageable pageable = PageRequest.of(page, productsPerPage);
        Page<Phone> randomPhones = phoneService.findRandomPhones(pageable);
        model.addAttribute("randomPhones", randomPhones);

        // Calculate total pages
        long totalProducts = phoneService.countProductsByBrand(brandId);
        int totalPages = totalProducts > 0 ? (int) Math.ceil((double) totalProducts / productsPerPage) : 0;
        model.addAttribute("totalPages", totalPages);

        return "random_products";
    }

    @GetMapping("/product-list")
    public String productList(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(required = false, defaultValue = "define") String sort,
                              Model model) {
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

    @GetMapping("/search")
    public String search(@RequestParam String keyword,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "9") int size,
                         Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Phone> searchResults = phoneService.findPhonesByNameContaining(keyword, pageable);
        model.addAttribute("searchResults", searchResults);
        model.addAttribute("totalPages", searchResults.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("size", size);

        return "search_results";
    }

    @GetMapping("/history")
    public String getHistory(@CookieValue(name="email", required = false) String email, Model model) {
        User user = userService.findUserByEmail(email);

        if (user != null) {
            List<ProductViewHistory> history = productViewHistoryService.getHistory(user.getId());
            model.addAttribute("history", history);
        }

        return "history";
    }

    @GetMapping("/favorite")
    public String getFavorites(@CookieValue(name="email", required = false) String email, Model model) {
        User user = userService.findUserByEmail(email);

        if (user != null) {
            List<Favorite> favorites = favoriteService.getFavoritesByUser(user);
            model.addAttribute("favorites", favorites);
        }

        return "favorite";
    }

    private String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


    @PostMapping("/cart/buyNow/{id}")
    public String buyNow(HttpServletRequest request,
                         @PathVariable Integer id,
                         @RequestParam(name = "quantity", defaultValue = "1") int quantity,
                         Model model) {
        String email = getCookieValue(request, "email");
        User user = userService.findUserByEmail(email);

        if (user == null) {
            return "redirect:/login";
        }

        Phone phone = phoneService.findPhoneById(id);
        cartService.addCartItem(phone, user, quantity);

        // Duplicate logic from checkout
        List<CartItem> cartItems = cartService.getCartItems(user);
        model.addAttribute("cartItems", cartItems);

        double total = 0;
        for (CartItem cartItem : cartItems) {
            total += cartItem.getPhone().getPrice() * cartItem.getQuantity();
        }
        model.addAttribute("total", total);

        return "cartToPay";
    }



}