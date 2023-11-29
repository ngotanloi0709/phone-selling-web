package com.fighting.phonesellingweb.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "phone_id", referencedColumnName = "id")
    private Phone phone;

    private int quantitySold;

    public Sale(Phone phone, int quantitySold) {
        this.phone = phone;
        this.quantitySold = quantitySold;
    }
}