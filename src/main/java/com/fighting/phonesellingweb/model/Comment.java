package com.fighting.phonesellingweb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "phone_id")
    private Phone phone;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String content;
    private String dateCreated;

    public Comment(Phone phone, User user, String content) {
        this.phone = phone;
        this.user = user;
        this.content = content;
    }

    @PrePersist
    protected void onCreate() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        this.dateCreated = dateFormat.format(currentDate);
    }
}
