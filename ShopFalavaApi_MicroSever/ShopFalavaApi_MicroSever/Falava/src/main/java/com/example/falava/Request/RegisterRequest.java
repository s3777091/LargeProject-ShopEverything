package com.example.falava.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    String email;
    String fullName;
    String password;
}
