package com.fighting.phonesellingweb.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private double price;
    private String color;
    private String ram;
    private String os;
    private String cpu;
    private String display;
    private String camera;
    private String batteryLife;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    @JsonManagedReference("phone-brand")
    private Brand brand;
}
