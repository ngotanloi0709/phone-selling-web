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
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    public void updateOrder(Order updatedOrder) {
        Order existingOrder = orderRepository.findById(updatedOrder.getId()).orElseThrow(() -> new RuntimeException("Order not found"));
        existingOrder.setStatus(updatedOrder.getStatus());
        orderRepository.save(existingOrder);
    }

    public void deleteOrder(int id) {
        orderRepository.deleteById(id);
    }


    public void addOrder(Order order) {
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItem.setOrder(order);
        }
        saveOrder(order); // Save the order after adding the OrderItems
    }
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

    public Order getOrderById(Integer id) {
        return orderRepository.findByIdWithOrderItems(id);
    }



    public List<OrderItem> getOrderItemsByOrderId(int id) {
        return orderItemRepository.findByOrderId(id);
    }



}