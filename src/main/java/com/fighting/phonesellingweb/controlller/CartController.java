package com.fighting.phonesellingweb.controlller;

import com.fighting.phonesellingweb.model.CartItem;
import com.fighting.phonesellingweb.model.Phone;
import com.fighting.phonesellingweb.model.User;
import com.fighting.phonesellingweb.service.CartService;
import com.fighting.phonesellingweb.service.PhoneService;
import com.fighting.phonesellingweb.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
@AllArgsConstructor
public class CartController {
    private UserService userService;
    private CartService cartService;
    private PhoneService phoneService;

    @GetMapping({"", "/"})
    public String getCart(@CookieValue(name="email", required = false) String email, Model model) {
        if (email != null) {
            User user = userService.findUserByEmail(email);
            model.addAttribute("user", user);
        }

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
    public String addCartItem(@CookieValue(name = "email", required = false) String email, @PathVariable Integer id, @RequestParam(name = "quantity", defaultValue = "1") int quantity) {
        User user = userService.findUserByEmail(email);
        Phone phone = phoneService.findPhoneById(id);
        cartService.addCartItem(phone, user, quantity);

        return "redirect:/";
    }

    @PostMapping("/update/{id}")
    public String updateCartItem(@CookieValue(name = "email", required = false) String email, @PathVariable Integer id, @RequestParam(name = "quantity", defaultValue = "1") int quantity) {
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
}
