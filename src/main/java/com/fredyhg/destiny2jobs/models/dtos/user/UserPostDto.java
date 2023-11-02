package com.fredyhg.destiny2jobs.models.dtos.user;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserPostDto {

    @Size(min=3, max=20, message="Name should be between 3 to 20 characters.")
    private String name;

    @Size(min=3, max=20, message="Last name should be between 3 to 20 characters.")
    private String lastName;

    @Email(message="Invalid email format.")
    private String email;

    @NotBlank(message="Password cannot be blank.")
    @Size(min=6, max=255, message="Password should be between 6 to 255 characters.")
    private String password;

    @NotBlank(message="Discord name cannot be blank.")
    private String discordName;

}
