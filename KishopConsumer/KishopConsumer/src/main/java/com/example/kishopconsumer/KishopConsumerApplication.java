package com.example.kishopconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.support.converter.JsonMessageConverter;

@SpringBootApplication
@EnableKafka
public class KishopConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(KishopConsumerApplication.class, args);
    }

    @Bean
    JsonMessageConverter converter(){
        return new JsonMessageConverter();
    }

}
