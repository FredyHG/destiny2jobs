package com.fredyhg.destiny2jobs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Destiny 2 jobs API", version = "1.0", description = "EndPoints Information"))
@SecurityScheme(name = "bearer", scheme = "bearer", bearerFormat = "JWT",type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER, paramName = "Bearer")
public class Destiny2jobsApplication {

    public static void main(String[] args) {
        SpringApplication.run(Destiny2jobsApplication.class, args);
    }

}
