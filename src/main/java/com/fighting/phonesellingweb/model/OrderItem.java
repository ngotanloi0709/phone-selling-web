package com.fighting.phonesellingweb.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "phone_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "comments", "viewHistory"})
    private Phone phone;

    private int quantity;
    private double price;

    public void setOrder(Order order) {
        this.order = order;
        // Không gọi addOrderItem ở đây để tránh đệ quy
    }

}