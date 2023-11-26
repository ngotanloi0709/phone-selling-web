package com.fighting.phonesellingweb.service;

import com.fighting.phonesellingweb.model.CartItem;
import com.fighting.phonesellingweb.model.Phone;
import com.fighting.phonesellingweb.model.User;
import com.fighting.phonesellingweb.repository.CartItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CartService {
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

    public void deleteCartItem(Integer id) {
        cartItemRepository.deleteById(id);
    }
}
