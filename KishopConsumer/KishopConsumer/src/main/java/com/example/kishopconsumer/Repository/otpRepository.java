package com.example.kishopconsumer.Repository;

import com.example.kishopconsumer.Entity.otpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface otpRepository extends JpaRepository<otpEntity,Long> {
    otpEntity findByOTP(String OTP);
    otpEntity findByEmail(String email);
}