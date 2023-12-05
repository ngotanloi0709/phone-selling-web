package com.fighting.phonesellingweb.service;

import com.fighting.phonesellingweb.model.*;
import com.fighting.phonesellingweb.repository.OrderItemRepository;
import com.fighting.phonesellingweb.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository; // Add this

    @Transactional
    public Order createOrder(User user, List<CartItem> cartItems) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(Order.OrderStatus.PENDING);

        // Save the order first to generate the id
        Order savedOrder = orderRepository.save(order);

        double totalAmount = 0;
        for (CartItem item : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setPhone(item.getPhone());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getPhone().getPrice());
            orderItem.setOrder(savedOrder); // Set the savedOrder with the generated id
            orderItemRepository.save(orderItem); // Save the OrderItem
            totalAmount += item.getPrice() * item.getQuantity();
        }

        savedOrder.setTotalAmount(totalAmount);
        return orderRepository.save(savedOrder); // Save the order again to update the total amount
    }

    public Order getOrderById(int id) {
        return orderRepository.findByIdWithOrderItems(id).orElse(null);
    }

    public List<OrderItem> getOrderItemsByOrderId(int id) {
        return orderItemRepository.findByOrderId(id);
    }
}