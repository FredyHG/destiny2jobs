package com.fredyhg.destiny2jobs.controllers;

import com.fredyhg.destiny2jobs.models.AuthenticationResponse;
import com.fredyhg.destiny2jobs.models.dtos.user.UserGetDto;
import com.fredyhg.destiny2jobs.models.dtos.user.UserPostDto;
import com.fredyhg.destiny2jobs.utils.models.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;


public interface UserController {

    @Operation(summary = "Create new account", description = "Anyone can make this request.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created successfully"),
            @ApiResponse(responseCode = "409", description = "Email already exists"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request")
    })
    ResponseEntity<AuthenticationResponse> createUser(UserPostDto user);

    @Operation(summary = "List all users", description = "To perform the request u need ADMIN role.", tags = "ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accounts listed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request")
    })
    ResponseEntity<Page<UserGetDto>> listAllUsers(Pageable pageable);

    @Operation(summary = "Delete user", description = "To perform the request u need ADMIN role.", tags = "ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not exists"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request")
    })
    ResponseEntity<ResponseMessage> deleteUser(UUID uuid);

    @Operation(summary = "Get user data", description = "The getCurrentUserData method uses the provided authentication token to retrieve and return the current user's data.", tags = "USER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get User Data successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid token or User not exist")
    })
    ResponseEntity<UserGetDto> getCurrentUserData(HttpServletRequest request, HttpServletResponse response);

    @Operation(summary = "Check if email is available to create account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email available"),
            @ApiResponse(responseCode = "401", description = "Email already registered")
    })
    ResponseEntity<ResponseMessage> isEmailAvailable(String email);

    @Operation(summary = "Check if discord is available to create account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Discord available"),
            @ApiResponse(responseCode = "401", description = "Discord already registered")
    })
    ResponseEntity<ResponseMessage> isDiscordAvailable(String email);
}
