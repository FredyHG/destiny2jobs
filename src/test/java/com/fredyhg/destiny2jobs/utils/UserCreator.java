package com.fredyhg.destiny2jobs.utils;

import com.fredyhg.destiny2jobs.enums.Role;
import com.fredyhg.destiny2jobs.models.UserModel;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;


public class UserCreator {

    public static UserModel createValidUserModel(){
        return UserModel.builder()
                .name("Jonh")
                .lastName("Snow")
                .email("email@email.com")
                .password("12345678")
                .discordName("JonhSnow122")
                .balance(0.0)
                .createAt(new Date())
                .role(Role.ROLE_USER)
                .build();

    }

}
