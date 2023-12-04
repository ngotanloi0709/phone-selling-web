package com.fighting.phonesellingweb.controlller;

import com.fighting.phonesellingweb.model.CartItem;
import com.fighting.phonesellingweb.model.User;
import com.fighting.phonesellingweb.service.CartService;
import com.fighting.phonesellingweb.service.UserService;
import com.fighting.phonesellingweb.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@ControllerAdvice
@AllArgsConstructor
public class GlobalControllerAdvice {
    private UserService userService;
    private CartService cartService;

    @ModelAttribute
    public void addUserAttributes(@CookieValue(name="email", required = false) String email, Model model, HttpServletResponse response) throws IOException {
        if (email != null) {
            User user = userService.findUserByEmail(email);
            if (user != null) {
                // section for user info
                model.addAttribute("user", user);
                // section for cart quantity
                List<CartItem> cartItems = cartService.getCartItems(userService.findUserByEmail(email));
                int cartItemsCount = 0;
                for (CartItem cartItem : cartItems) {
                    cartItemsCount += cartItem.getQuantity();
                }
                model.addAttribute("cartItemsCount", cartItemsCount);
                // section for avatar
                if (user.getAvatar() != null) {
                    model.addAttribute("base64Avatar", Base64.getEncoder().encodeToString(user.getAvatar()));
                }
            } else {
                model.addAttribute("user", null);
                model.addAttribute("base64Avatar", null);
                CookieUtil.clearCookies(response);
            }
        }
    }
}