package com.example.kishopconsumer.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "userId", nullable = false, updatable = false)
    private long userId;

    @Column(name = "full_name")
    @NotNull(message = "Full Name cannot be null")
    private String fullName;

    @Email(message = "Email must be @gmail.com")
    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "token")
    private String token;

    @Size(min = 6, max = 20, message = "Password must be from 6 to 20 characters")
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "avatar", nullable = false)
    private String avatar;

    @Column(name = "status", columnDefinition = "BOOLEAN")
    private boolean status;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "modified_at")
    private Timestamp modifiedAt;

    @OneToOne
    private UserBuyingEntity userBuyingEntity;
}
