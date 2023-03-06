package com.example.kishopconsumer.Service;

import com.example.kishopconsumer.Entity.UserBuyingEntity;
import com.example.kishopconsumer.Model.UserReceiver;

public interface UserImpl {
    void sendMailRegister(UserReceiver userReceiver);
    void saveUserRegister(UserReceiver userReceiver);
    UserBuyingEntity createBuying();
}
