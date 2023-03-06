package com.example.kishopconsumer.Entity;


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
public class ProductCatagory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "productcatagoryid", nullable = false, updatable = false)
    private long id;

    @Column(name = "producttypeimage", nullable = false, length = 100)
    private String productimage;

    @Column(name = "producttype", nullable = false, length = 20)
    private String producttype;

}