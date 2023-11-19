package com.fighting.phonesellingweb;

import com.fighting.phonesellingweb.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@AllArgsConstructor
public class PhoneSellingWebApplication {
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(PhoneSellingWebApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(){
        return args -> {
//            userService.register("ngotanloi0709@gmail.com", "loi@2003", "Ngô Tấn Lợi", "0123456789");
//            userService.setRole("ngotanloi0709@gmail.com", Role.ROLE_ADMIN);
        };
    }

}
