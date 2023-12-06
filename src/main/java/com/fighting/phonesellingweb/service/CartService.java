package com.fighting.phonesellingweb.service;

import com.fighting.phonesellingweb.model.*;
import com.fighting.phonesellingweb.repository.CartItemRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CartService.class);

    private CartItemRepository cartItemRepository;

    public List<CartItem> getCartItems(User user) {
        return cartItemRepository.findByUser(user);
    }

    public void addCartItem(Phone phone, User user, int quantity) {
        CartItem cartItem = cartItemRepository.findByUserIdAndPhoneId(user.getId(), phone.getId());

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = new CartItem();
            cartItem.setQuantity(quantity);
            cartItem.setUser(user);
            cartItem.setPhone(phone);
        }

        cartItemRepository.save(cartItem);
    }

    public void updateCartItem(Integer id, int quantity) {
        CartItem cartItem = cartItemRepository.findById(id).get();
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    public void deleteCartItem(Integer id) {
        cartItemRepository.deleteById(id);
    }

    public void clearCart(User user, Order order) {
        try {
            List<CartItem> cartItems = cartItemRepository.findByUser(user);
            for (CartItem item : cartItems) {
                cartItemRepository.delete(item);
            }
        } catch (Exception e) {
            LOGGER.error("Error when clearing the cart: ", e);
        }
    }
}