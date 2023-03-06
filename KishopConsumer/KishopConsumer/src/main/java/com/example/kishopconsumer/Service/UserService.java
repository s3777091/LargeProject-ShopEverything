package com.example.kishopconsumer.Service;

import com.example.kishopconsumer.Entity.UserBuyingEntity;
import com.example.kishopconsumer.Entity.UserEntity;
import com.example.kishopconsumer.Model.UserReceiver;
import com.example.kishopconsumer.Repository.UserBuyingRepository;
import com.example.kishopconsumer.Repository.UserRepository;
import com.example.kishopconsumer.Tool.Mapper;
import jakarta.mail.internet.MimeMessage;
import net.bytebuddy.utility.RandomString;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Objects;

import static com.example.kishopconsumer.Config.Constant.*;

@Service
public class UserService implements UserImpl{

    @Autowired
    JavaMailSender mailSender;


    @Autowired
    UserRepository userRepository;

    @Autowired
    UserBuyingRepository userBuyingRepository;

    private String email_format(String mail) {
        return mail.substring(mail.indexOf("@"));
    }


    @Override
    public void sendMailRegister(UserReceiver userReceiver) {
        UserEntity user = userRepository.findByEmail(userReceiver.getEmail());
        MimeMessage message = mailSender.createMimeMessage();
        try {
            if (user == null) {
                System.out.println("Re-again");
            }
        } catch (Exception e) {
            System.out.println("Repeat step");
            sendMailRegister(userReceiver);
        } finally {
            try {
                String html_template = Jsoup.parse(new File("src/main/resources/templates/ValidateMail.html"), "UTF-8").toString();
                MimeMessageHelper helper = new MimeMessageHelper(message);

                helper.setFrom(EMAIL_POSTMASTER, "KISHOP");
                helper.setTo(userReceiver.getEmail());
                helper.setSubject("Kishop send verify email registration");

                html_template = html_template.replace("CUSTOMENAME", userReceiver.getEmail());
                String verifyURL = URL_HOSTING_PRODUCER + "/verify?token=" + user.getToken();

                html_template = html_template.replace("[[URL]]", verifyURL);
                helper.setText(html_template, true);
                mailSender.send(message);
                System.out.println("Sending");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    @Async
    public void saveUserRegister(UserReceiver userReceiver) {
        UserEntity userRegister = userRepository.findByEmail(userReceiver.getEmail());
        if (userRegister != null) {
            //Email already register
            System.out.println("User Already in system");
        } else if (!Objects.equals(email_format(userReceiver.getEmail()), "@gmail.com")) {
            //Wrong email format
            System.out.println("User Have Wrong input mail");
        }
        userRegister = Mapper.toUser(userReceiver);
        userRepository.save(userRegister);
    }


    @Override
    @Async
    public UserBuyingEntity createBuying() {
        String temp = RandomString.make(32);
        UserBuyingEntity userBuying = new UserBuyingEntity();
        UserBuyingEntity userChecking = userBuyingRepository.findByToken(temp);
        try {
            if (userChecking != null) {
                System.out.println("Account Number can't have same number pls create again");
            }
        } catch (Exception e) {
            createBuying();
        } finally {
            Objects.requireNonNull(userBuying).setAccountBalance(Double.parseDouble("0"));
            userBuying.setTotalorder(Double.parseDouble("0"));
            userBuying.setToken(temp);
            userBuying.setBuyinglocation("Nothing Here");

            userBuyingRepository.save(userBuying);
        }
        return userBuying;
    }

    
}
