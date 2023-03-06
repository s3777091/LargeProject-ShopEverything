package com.example.falava.Entity.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserBuyingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String token;

    @Column(name = "balance", scale = 3, precision = 15, columnDefinition = "DECIMAL(15,3)")
    private double accountBalance;

    @Column(name = "phone")
    private String phonenumber;

    @Column(name = "location")
    private String buyinglocation;

    @Column(name = "total_order")
    private double totalorder;

}
