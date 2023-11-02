package com.fredyhg.destiny2jobs.models.dtos.user;

import com.fredyhg.destiny2jobs.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class UserGetDto {

    private String name;

    private String lastName;

    private String email;

    private String discordName;

    private Double balance;

    private Date createAt;

    private Role role;

}
