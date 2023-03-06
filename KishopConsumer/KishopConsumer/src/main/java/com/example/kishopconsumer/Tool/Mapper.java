package com.example.kishopconsumer.Tool;

import com.example.kishopconsumer.Entity.UserEntity;
import com.example.kishopconsumer.Model.UserReceiver;
import net.bytebuddy.utility.RandomString;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.sql.Timestamp;

public class Mapper {
    public static UserEntity toUser(UserReceiver userReceiver) {
        UserEntity user = new UserEntity();
        user.setFullName(userReceiver.getFullName());
        user.setEmail(userReceiver.getEmail());
        String hash = BCrypt.hashpw(userReceiver.getPassword(), BCrypt.gensalt(12));
        String randomCode = RandomString.make(32);
        user.setToken(randomCode);
        user.setPassword(hash);
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        user.setStatus(false);
        user.setAvatar("https://png.pngtree.com/png-vector/20220817/ourmid/pngtree-cartoon-man-avatar-vector-ilustration-png-image_6111064.png");
        return user;
    }
}
