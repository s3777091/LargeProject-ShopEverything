package com.example.falava.Controller;


import com.example.falava.Entity.User.UserBuyingEntity;
import com.example.falava.Entity.User.UserEntity;
import com.example.falava.Entity.User.otpEntity;
import com.example.falava.Modal.UserReceiver;
import com.example.falava.Repository.UserBuyingRepository;
import com.example.falava.Repository.UserRepository;
import com.example.falava.Repository.otpRepository;
import com.example.falava.Request.*;
import com.example.falava.Service.UserImpl;
import jakarta.servlet.http.HttpServletRequest;
import net.bytebuddy.utility.RandomString;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
public class UserController {

    @Autowired
    KafkaTemplate<String, Object> controlTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    otpRepository otp;

    @Autowired
    UserImpl usertask;

    @Autowired
    UserBuyingRepository userBuyingRepository;


    private boolean CheckingAuthenticate(String email) {
        UserEntity user = userRepository.findByEmail(email);
        return user.isStatus();
    }


    @PostMapping("/api/v1/user/register")
    public ResponseEntity<Object> getRegister(@RequestBody RegisterRequest registerRequest) {
        JSONObject total_pr = new JSONObject();

        total_pr.put("success", true);
        total_pr.put("status", "Success create new user");

        UserReceiver userReceiver = new UserReceiver();

        //Need Checking Here
        userReceiver.setEmail(registerRequest.getEmail());
        userReceiver.setFullName(registerRequest.getFullName());
        userReceiver.setPassword(registerRequest.getPassword());

        controlTemplate.send("UserRegister", userReceiver);

        return new ResponseEntity<>(total_pr.toMap(), HttpStatus.OK);
    }


    @GetMapping("/verify")
    public ResponseEntity<Object> verifyUser(@Param("token") String token, HttpServletRequest request) throws URISyntaxException {
        try {
            if (usertask.verify(token)) {
                controlTemplate.send("UserVerifyAccount", token);
                return new ResponseEntity<>("Success Create user", HttpStatus.OK);
            } else {
                throw new Exception("User Already register");
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/api/v1/login")
    @Async
    public CompletableFuture<ResponseEntity<Object>> login(@RequestBody LoginRequest loginRequest) {
        try {
            UserEntity user = userRepository.findByEmail(loginRequest.getEmail());
            //user is existing ?
            if (user != null) {
                if (!BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {
                    //Wrong password
                    return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
                } else {
                    //Login success
                    //Return 200
                    controlTemplate.send("ActiveUser", user);
                    return CompletableFuture.completedFuture(new ResponseEntity<>(user, HttpStatus.OK));
                }
            } else {
                //User not existing or already logout
                return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
            }
        } catch (Exception ex) {
            //Return ???
            return CompletableFuture.completedFuture(new ResponseEntity<>(ex, HttpStatus.FORBIDDEN));
        }
    }

    @GetMapping("/api/logout")
    public ResponseEntity<Object> LogoutUser(@Param("email") String email) {
        try {
            UserEntity user = userRepository.findByEmail(email);
            user.setStatus(false);
            userRepository.save(user);
            return new ResponseEntity<>("Logout success", HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/api/send_forgot_password")
    @Async
    public CompletableFuture<ResponseEntity<Object>> forgotPassword(@RequestBody resetRequest resetRequest) {
        try {
            UserEntity user = userRepository.findByEmail(resetRequest.getEmail());
            if (user == null) {
                return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
            }


            //Active this while we want user change password when they already login
/*            else if (!user.isStatus()) {
                return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.FORBIDDEN));
            }*/

            String token = RandomString.make(5);

            otpEntity otpCreate = new otpEntity();
            otpCreate.setOTP(token);
            otpCreate.setEmail(user.getEmail());
            otp.save(otpCreate);

            controlTemplate.send("ForgotPassword", otpCreate);
            return CompletableFuture.completedFuture(new ResponseEntity<>("Success send forgot password", HttpStatus.OK));
        } catch (Exception ex) {
            return CompletableFuture.completedFuture(new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/api/v1/update/user_adress")
    public ResponseEntity<Object> updateAdress(@RequestBody UserAdress userAdress) {
        JSONObject total_pr = new JSONObject();
        total_pr.put("success", true);

        UserEntity userChecking = userRepository.findByEmail(userAdress.getEmailUser());
        if (userChecking == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        long id = userChecking.getUserBuyingEntity().getId();
        UserBuyingEntity userBuying = userBuyingRepository.findById(id);

        userBuying.setBuyinglocation(userAdress.getLocation());
        userBuying.setPhonenumber(userAdress.getPhone());

        controlTemplate.send("UpdateLocation", userBuying);
        total_pr.put("status", "Success update new " + userAdress.getLocation());
        return new ResponseEntity<>(total_pr.toMap(), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/api/v1/view/user_profile")
    @Async
    public CompletableFuture<ResponseEntity<Object>> getProfile(@Param("email") String email) {
        JSONObject total_pr = new JSONObject();
        total_pr.put("success", true);

        UserEntity userChecking = userRepository.findByEmail(email);
        if (userChecking == null) {
            return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
        }

        if (!userChecking.isStatus()) {
            //Pls Login
            return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.FORBIDDEN));
        }

        total_pr.put("results", userChecking);

        total_pr.put("status", "Success get data user " + userChecking.getEmail());
        return CompletableFuture.completedFuture(new ResponseEntity<>(total_pr.toMap(), new HttpHeaders(), HttpStatus.OK));
    }

}
