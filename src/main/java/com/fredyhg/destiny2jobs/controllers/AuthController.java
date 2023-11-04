package com.fredyhg.destiny2jobs.controllers;


import com.fredyhg.destiny2jobs.models.AuthenticationResponse;
import com.fredyhg.destiny2jobs.models.dtos.auth.AuthenticationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;


public interface AuthController {


    @Operation(summary = "Authentication user", description = "Anyone can make this request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created successfully"),
            @ApiResponse(responseCode = "400", description = "User credencials error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request")
    })
    ResponseEntity<AuthenticationResponse> authenticate(AuthenticationDto authenticationDto);

    @Operation(summary = "Authentication user", description = "Anyone can make this request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created successfully"),
            @ApiResponse(responseCode = "400", description = "User credencials error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request")
    })
    void refresToken(HttpServletRequest request, HttpServletResponse response);

}
