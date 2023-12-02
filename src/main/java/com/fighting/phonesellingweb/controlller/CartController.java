package com.fighting.phonesellingweb.controlller;

import com.fighting.phonesellingweb.model.CartItem;
import com.fighting.phonesellingweb.model.Phone;
import com.fighting.phonesellingweb.model.User;
import com.fighting.phonesellingweb.service.CartService;
import com.fighting.phonesellingweb.service.PhoneService;
import com.fighting.phonesellingweb.service.UserService;
import com.fighting.phonesellingweb.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/cart")
@AllArgsConstructor
public class CartController {
    private UserService userService;
    private CartService cartService;
    private PhoneService phoneService;

    @GetMapping({"", "/"})
    public String getCart(HttpServletRequest request, Model model) {
        String email = getCookieValue(request, "email");
        List<CartItem> cartItems = cartService.getCartItems(userService.findUserByEmail(email));
        model.addAttribute("cartItems", cartItems);

        double total = 0;

        for (CartItem cartItem : cartItems) {
            total += cartItem.getPhone().getPrice() * cartItem.getQuantity();
        }

        model.addAttribute("total", total);

        return "cart";
    }

    @PostMapping("/add/{id}")
    public String addCartItem(HttpServletRequest request, @PathVariable Integer id, @RequestParam(name = "quantity", defaultValue = "1") int quantity) {
        String email = getCookieValue(request, "email");
        User user = userService.findUserByEmail(email);
        Phone phone = phoneService.findPhoneById(id);
        cartService.addCartItem(phone, user, quantity);

        return "redirect:/";
    }

    @PostMapping("/update/{id}")
    public String updateCartItem(HttpServletRequest request, @PathVariable Integer id, @RequestParam(name = "quantity", defaultValue = "1") int quantity) {
        String email = getCookieValue(request, "email");
        User user = userService.findUserByEmail(email);
        Phone phone = phoneService.findPhoneById(id);
        cartService.addCartItem(phone, user, quantity);

        return "redirect:/cart";
    }

    @PostMapping("/delete/{id}")
    public String deleteCartItem(@PathVariable Integer id) {
        cartService.deleteCartItem(id);

        return "redirect:/cart";
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


    @PostMapping("/checkout")
    public String checkout(HttpServletRequest request, Model model) {
        String email = getCookieValue(request, "email");
        User user = userService.findUserByEmail(email);

        if (user != null) {
            model.addAttribute("user", user);
        } else {
            return "redirect:/login";
        }

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