package com.fighting.phonesellingweb.repository;

import com.fighting.phonesellingweb.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderId(int id);
}
