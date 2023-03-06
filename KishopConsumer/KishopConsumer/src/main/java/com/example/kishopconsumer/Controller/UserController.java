package com.example.kishopconsumer.Controller;

import com.example.kishopconsumer.Entity.UserEntity;
import com.example.kishopconsumer.Entity.otpEntity;
import com.example.kishopconsumer.Repository.UserRepository;
import com.example.kishopconsumer.Repository.otpRepository;
import com.example.kishopconsumer.Service.UserImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;


@RestController
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    otpRepository otpRes;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/api/forgot_password")
    public ResponseEntity<Object> getPasswordForm(@Param("OTP") String OTP, @Param("Password") String password) {
        otpEntity checkingOTP = otpRes.findByOTP(OTP);
        if (checkingOTP == null) {
            return new ResponseEntity<>("Don't Have this token", HttpStatus.FORBIDDEN);
        } else {

            String hash = BCrypt.hashpw(password, BCrypt.gensalt(12));
            UserEntity user = userRepository.findByEmail(checkingOTP.getEmail());
            user.setPassword(hash);
            userRepository.save(user);
            return new ResponseEntity<>("Change password success", HttpStatus.FORBIDDEN);
        }
    }


}
