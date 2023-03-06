package com.example.falava.Repository;

import com.example.falava.Entity.User.otpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface otpRepository extends JpaRepository<otpEntity,Long> {
    otpEntity findByOTP(String OTP);
    otpEntity findByEmail(String email);
}