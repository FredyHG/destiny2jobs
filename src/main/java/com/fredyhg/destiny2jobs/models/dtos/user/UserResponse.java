package com.fredyhg.destiny2jobs.models.dtos.user;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserResponse {

    private UUID id;

    private String name;

    private String lastName;

    private String email;

    private String discordName;


}
