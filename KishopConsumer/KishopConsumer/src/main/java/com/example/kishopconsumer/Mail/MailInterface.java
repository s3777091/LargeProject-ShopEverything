package com.example.kishopconsumer.Mail;

import com.example.kishopconsumer.Entity.UserEntity;
import com.example.kishopconsumer.Entity.otpEntity;
import com.example.kishopconsumer.Model.CheckingControlModel;

public interface MailInterface {
    void sendEmailOTP(otpEntity otp);
    void sendEmailThanks(CheckingControlModel check);
}
