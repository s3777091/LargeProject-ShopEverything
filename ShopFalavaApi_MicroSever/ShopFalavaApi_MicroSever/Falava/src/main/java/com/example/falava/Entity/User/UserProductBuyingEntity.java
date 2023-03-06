package com.example.falava.Entity.User;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import java.util.Date;

@Getter
@Setter
@Entity
public class UserProductBuyingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Date date;

    private String productname;
    private String productImage;

    private String price;

    private String type;

    private String status;

    @Pattern(regexp="-?\\d+(\\.\\d+)?", message = "Not is number format")
    @JsonProperty("amount")
    @Column(name = "amount", scale = 3, precision = 15, columnDefinition = "DECIMAL(15,3)")
    private double amount;

    public UserProductBuyingEntity() {
    }

    @ManyToOne
    private UserBuyingEntity userBuyingEntity;

    public UserProductBuyingEntity(Date date, String product_name, String price, String type, String status, double amount, UserBuyingEntity userBuyingEntity) {
        this.date = date;
        this.productname = product_name;
        this.price = price;
        this.type = type;
        this.status = status;
        this.amount = amount;
        this.userBuyingEntity = userBuyingEntity;
    }
}
