package com.example.falava.Request;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;


@Getter
@Setter
public class CheckOutRequest {
    String phoneNumber;
    String usermail;
    String address;
    String total;
}
