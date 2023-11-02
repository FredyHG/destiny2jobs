package com.fredyhg.destiny2jobs.models.dtos.user;

import com.fredyhg.destiny2jobs.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkerResponse {
    private UUID id;

    private String name;

    private String lastName;

    private String email;

    private String discordName;

    private Role role;
}
