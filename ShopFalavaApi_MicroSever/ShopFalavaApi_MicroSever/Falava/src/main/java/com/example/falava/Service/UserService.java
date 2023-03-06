package com.example.falava.Service;

import com.example.falava.Entity.User.UserEntity;
import com.example.falava.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserImpl{


    @Autowired
    UserRepository userRepository;


    @Override
    public boolean verify(String token) {
        UserEntity userRegister = userRepository.findByToken(token);
        return userRegister.getToken() != null || !userRegister.isStatus();
    }





}
