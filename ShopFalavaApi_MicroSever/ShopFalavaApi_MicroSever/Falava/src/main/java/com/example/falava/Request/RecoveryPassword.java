package com.example.falava.Request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RecoveryPassword {
    String password;
    String confirm_password;
}