package com.example.kishopconsumer.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Setter
@Getter
public class otpEntity {
    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "otp", length = 5)
    private String OTP;
}
