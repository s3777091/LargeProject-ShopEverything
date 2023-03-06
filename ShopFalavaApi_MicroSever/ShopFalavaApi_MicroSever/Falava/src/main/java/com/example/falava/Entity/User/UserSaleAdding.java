package com.example.falava.Entity.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;


@Getter
@Setter
@Entity
public class UserSaleAdding {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String salesproduct;

    private String salesexpire;

    private String salestoken;

    @Pattern(regexp="-?\\d+(\\.\\d+)?", message = "Not is number format")
    @JsonProperty("sales_value")
    @Column(name = "sales_value", scale = 3, precision = 15, columnDefinition = "DECIMAL(15,3)")
    private double sales_value;

    public UserSaleAdding() {
    }

    @ManyToOne
    private UserBuyingEntity userBuyingEntity;

    public UserSaleAdding(String sales_product, String sales_expire, String sales_token,double sales_value, UserBuyingEntity userBuyingEntity) {
        this.salesproduct = sales_product;
        this.salesexpire = sales_expire;
        this.salestoken = sales_token;
        this.sales_value = sales_value;
        this.userBuyingEntity = userBuyingEntity;
    }
}
