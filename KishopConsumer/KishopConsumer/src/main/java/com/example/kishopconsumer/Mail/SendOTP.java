package com.example.kishopconsumer.Mail;

import com.example.kishopconsumer.Entity.UserEntity;
import com.example.kishopconsumer.Entity.otpEntity;
import com.example.kishopconsumer.Repository.otpRepository;
import com.google.common.util.concurrent.AbstractScheduledService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

public class SendOTP extends AbstractScheduledService {

    MailInterface mailInterface;
    otpEntity otpCheck;
    otpRepository otp;



    int count = 0;

    public SendOTP(MailInterface mailInterface, otpEntity otpEntity, otpRepository otp) {
        this.mailInterface = mailInterface;
        this.otpCheck = otpEntity;
        this.otp = otp;
    }

    @Override
    protected void runOneIteration() {
        try {
            //Send Time one but user not validate the OTP
            // if count = 0 => first send email
            // if count = 1 => Second send email
            //if count = 1 && user validate the otp => Stop send email
            if (count == 0) {
                mailInterface.sendEmailOTP(otpCheck);
                count++;
            } else if (count == 1 && otpCheck.getOTP() != null) {
                mailInterface.sendEmailOTP(otpCheck);
                count++;
            } else if (count == 1 && otpCheck.getOTP() == null) {
                otp.delete(otpCheck);
                stopAsync();
            } else if (count == 2){
                otp.delete(otpCheck);
                stopAsync();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedRateSchedule(0, 1, TimeUnit.MINUTES);
    }
}
