package com.fighting.phonesellingweb.repository;

import com.fighting.phonesellingweb.model.CartItem;
import com.fighting.phonesellingweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    public List<CartItem> findByUser(User user);
    public CartItem findByUserIdAndPhoneId(Integer userId, Integer phoneId);
}
