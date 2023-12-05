package com.fighting.phonesellingweb.repository;

import com.fighting.phonesellingweb.model.CartItem;
import com.fighting.phonesellingweb.model.Phone;
import com.fighting.phonesellingweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    public List<CartItem> findByUser(User user);
    List<CartItem> findByUserAndPhoneIn(User user, List<Phone> phones);
    CartItem findByUserIdAndPhoneId(Integer userId, Integer phoneId);
}
