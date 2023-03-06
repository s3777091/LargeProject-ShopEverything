package com.example.kishopconsumer.Service;

import com.example.kishopconsumer.Entity.*;
import com.example.kishopconsumer.Mail.MailInterface;
import com.example.kishopconsumer.Mail.SendOTP;
import com.example.kishopconsumer.Model.CheckingControlModel;
import com.example.kishopconsumer.Model.UserReceiver;
import com.example.kishopconsumer.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;


@Service
public class KafkaListenerTotal {

    @Autowired
    MailInterface mailInterface;

    @Autowired
    otpRepository otpRes;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserImpl userController;

    @Autowired
    UserBuyingRepository userBuyingRepository;

    @Autowired
    UserOrderRepository userOrderRepository;

    @Autowired
    ProductRepository productRepository;


    @KafkaListener(id = "VerifyGroup", topics = "UserVerifyAccount")
    public void ListenVerify(String token) {
        System.out.println("Get Token" + token);
        UserEntity user = userRepository.findByToken(token);
        user.setToken(null);
        user.setStatus(true);
        user.setUserBuyingEntity(userController.createBuying());
        userRepository.save(user);
    }

    @KafkaListener(id = "SaveRegisterGroup", topics = "UserRegister")
    public void ListenRegister(UserReceiver data){
        System.out.println("Start Save user" + data.getFullName());
        userController.saveUserRegister(data);

        userController.sendMailRegister(data);
    }

    @KafkaListener(id = "ActiveUserLogin", topics = "ActiveUser")
    public void ListenAtiveLogin(UserEntity user){
        System.out.println("Start Active user" + user.getFullName());
        user.setStatus(true);
        userRepository.save(user);
    }

    @KafkaListener(id = "ForgotPasswordGroup", topics = "ForgotPassword")
    public void ListenForgotPassword(otpEntity otps){
        System.out.println("Start Active user" + otps.getEmail());
        SendOTP otp = new SendOTP(mailInterface, otps, otpRes);
        otp.startAsync();
    }

    @KafkaListener(id = "UpdateLocationGroup", topics = "UpdateLocation")
    public void ListenUpdateAddress(UserBuyingEntity user){
        userBuyingRepository.save(user);
    }

    @KafkaListener(id = "BuyingProductGroup", topics = "BuyingKiProduct")
    public void ListenCreateBuyingProduct(UserProductBuyingEntity buying){

        UserProductBuyingEntity st = userOrderRepository.findByProductname(buying.getProductname());
        if (st == null) {
            userOrderRepository.save(buying);
        } else {
            st.setDate(new Timestamp(System.currentTimeMillis()));
            st.setAmount(st.getAmount() + buying.getAmount());
            userOrderRepository.save(st);
        }
    }

    @KafkaListener(id = "deletePaymentGroup", topics = "deletePayment")
    public void ListenDeleteOrder(UserProductBuyingEntity buying){
        userOrderRepository.delete(buying);
    }


    @KafkaListener(id = "addProductGroup", topics = "addKiProduct")
    public void addProducer(Product_items st){
        productRepository.save(st);
    }


    @KafkaListener(id = "CheckingOutGroup", topics = "CheckingOut")
    public void CheckingOut(UserProductBuyingEntity e){
        UserProductBuyingEntity st = userOrderRepository.findByProductname(e.getProductname());
        st.setStatus("On-going");
        userOrderRepository.save(st);
    }

    @KafkaListener(id = "SendMailCheckingOutGroup", topics = "SendMailCheckingOut")
    public void SendMailCheckingOut(CheckingControlModel e){
        mailInterface.sendEmailThanks(e);
    }

    @KafkaListener(id = "SendSearchPostGroup", topics = "SendSearchPost")
    public void SendSearchPost(Product_items st){
        productRepository.save(st);
    }

}
