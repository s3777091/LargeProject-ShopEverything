package com.example.falava.Entity.User;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Product_items {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "productid", nullable = false, updatable = false)
    private long id;

    @Column(name = "productname", length=100)
    private String productname;

    @Column(name = "productprice", length=20)
    private double price;

    @Column(name = "product_link", length=10485760)
    private String productlink;

    @Column(name = "imageurl", length =10485760)
    private String imgURL;

    @ManyToOne
    private ProductCatagory productCatagory;
}
