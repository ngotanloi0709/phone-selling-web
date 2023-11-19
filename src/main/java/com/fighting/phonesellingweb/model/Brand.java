package com.fighting.phonesellingweb.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @OneToMany(mappedBy = "brand", fetch = FetchType.EAGER)
    @JsonBackReference("phone-brand")
    @ToString.Exclude
    private List<Phone> phones;

    public Brand(String name) {
        this.name = name;
    }
}
