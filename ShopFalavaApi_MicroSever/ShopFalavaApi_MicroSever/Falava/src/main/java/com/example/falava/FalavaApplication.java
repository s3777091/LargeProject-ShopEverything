package com.example.falava;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
public class FalavaApplication {
    public static void main(String[] args) {
        SpringApplication.run(FalavaApplication.class, args);
    }

    @Bean
    public NewTopic SendSearchPost()   {
        return new NewTopic("SendSearchPost", 1 , (short) 1);
    }

    @Bean
    public NewTopic SendMail()  {
        return new NewTopic("SendMailCheckingOut", 1 , (short) 1);
    }

    @Bean
    public NewTopic CheckOut() {
        return new NewTopic("CheckingOut", 1 , (short) 1);
    }

    @Bean
    public NewTopic addProducer() {
        return new NewTopic("addKiProduct", 1 , (short) 1);
    }

    @Bean
    public NewTopic deletePayment() {
        return new NewTopic("deletePayment", 1 , (short) 1);
    }

    @Bean
    public NewTopic addBuyingProduct() {
        return new NewTopic("BuyingKiProduct", 1 , (short) 1);
    }

    @Bean
    public NewTopic ForgotPassword(){
        return new NewTopic("ForgotPassword", 1 , (short) 1);
    }

    @Bean
    public NewTopic LoginUser(){
        return new NewTopic("ActiveUser", 1 , (short) 1);
    }

    @Bean
    public NewTopic sendVerify(){
        return new NewTopic("UserVerifyAccount", 1 , (short) 1);
    }

    @Bean
    public NewTopic SaveUserRegister(){
        return new NewTopic("UserRegister", 1 , (short) 1);
    }

    @Bean
    public NewTopic UpdateLocation(){
        return new NewTopic("UpdateLocation", 1 , (short) 1);
    }

}
