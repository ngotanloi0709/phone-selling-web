package com.fighting.phonesellingweb.repository;

import com.fighting.phonesellingweb.model.Order;
import com.fighting.phonesellingweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUser(User user);

//    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems WHERE o.id = :id")
//    Optional<Order> findByIdWithOrderItems(@Param("id") int id);
    @Query("SELECT o FROM Order o WHERE o.id = :id")
    Order findByIdWithOrderItems(@Param("id") int id);

    List<Order> findByUserId(int userId);
    Optional<Order> findTopByUserOrderByOrderDateDesc(User user);

}