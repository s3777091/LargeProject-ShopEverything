package com.example.kishopconsumer.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    String email;
    String fullName;
    String password;
}
