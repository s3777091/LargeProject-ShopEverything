package com.example.kishopconsumer.Mail;

import com.example.kishopconsumer.Entity.UserEntity;
import com.example.kishopconsumer.Entity.otpEntity;
import com.example.kishopconsumer.Model.CheckingControlModel;
import com.example.kishopconsumer.Repository.UserRepository;
import com.example.kishopconsumer.Repository.otpRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import net.bytebuddy.utility.RandomString;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

import static com.example.kishopconsumer.Config.Constant.EMAIL_POSTMASTER;
import static com.example.kishopconsumer.Config.Constant.URL_HOSTING;

@Service
public class mailService implements MailInterface{

    @Autowired
    UserRepository userRepository;

    @Autowired
    JavaMailSender mailSender;


    @Autowired
    private otpRepository otpRepository;

    public void sendEmailOTP(otpEntity otp) {
        try {
            UserEntity user = userRepository.findByEmail(otp.getEmail());

            String toAddress = user.getEmail();

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            String html_template = Jsoup.parse(new File("src/main/resources/templates/OTPSend.html"), "UTF-8").toString();

            html_template = html_template.replace("[[CUSTOMERNAME]]", user.getFullName());
            String validateCode = URL_HOSTING + "/api/forgot_password?OTP=" + otp.getOTP();
            html_template = html_template.replace("[[URL]]", validateCode);
            //Send this token
            helper.setFrom(EMAIL_POSTMASTER, "KISHOP");
            helper.setTo(toAddress);
            helper.setSubject("KISHOP send OTP to reset your password");

            helper.setText(html_template, true);
            mailSender.send(message);

        } catch (MessagingException | IOException e) {
            System.out.println("EXCEPTION -- " + e);
        }
    }

    @Override
    public void sendEmailThanks(CheckingControlModel check) {
        try {
            String toAddress = check.getEmail();

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            String html_template = Jsoup.parse(new File("src/main/resources/templates/Temp.html"), "UTF-8").toString();

            html_template = html_template.replace("[[TOTAL]]", check.getTotal());
            html_template = html_template.replace("[[PHONE]]",
                    check.getPhone().concat(" ").concat("at address: ").concat(check.getAddress()));
            //Send this token
            helper.setFrom(EMAIL_POSTMASTER, "KISHOP");
            helper.setTo(toAddress);
            helper.setSubject("KISHOP send thank you to you");

            helper.setText(html_template, true);
            mailSender.send(message);
        } catch (MessagingException | IOException e) {
            System.out.println("EXCEPTION -- " + e);
        }
    }
}
