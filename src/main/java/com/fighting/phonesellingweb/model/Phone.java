package com.fighting.phonesellingweb.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    private String memory;
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

    @OneToMany(mappedBy = "phone")
    private List<Comment> comments;

    public Phone(String name, String description, double price, String color, String memory, String os, String cpu, String display, String camera, String batteryLife, String imageUrl, Brand brand) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.color = color;
        this.memory = memory;
        this.os = os;
        this.cpu = cpu;
        this.display = display;
        this.camera = camera;
        this.batteryLife = batteryLife;
        this.imageUrl = imageUrl;
        this.brand = brand;
    }
}
