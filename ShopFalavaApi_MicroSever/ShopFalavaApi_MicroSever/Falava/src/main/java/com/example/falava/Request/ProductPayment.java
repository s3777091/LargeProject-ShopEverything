package com.example.falava.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductPayment {
    String email;
    long product_id;
    long amount;


}
