package com.fredyhg.destiny2jobs.controllers;

import com.fredyhg.destiny2jobs.models.dtos.custompackage.CustomPackagePostDto;
import com.fredyhg.destiny2jobs.models.dtos.custompackage.CustomPackageResponse;
import com.fredyhg.destiny2jobs.utils.models.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;


public interface CustomPackageController {


    @Operation(summary = "Create new account", description = "To perform this request you need role ADMIN or USER, WORKERS does not perform this request", tags = {"USER", "ADMIN"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Custom Package created successfully"),
            @ApiResponse(responseCode = "409", description = "Package already exists or User does not have balance"),
            @ApiResponse(responseCode = "400", description = "Worker does not allowed to perform this request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request")
    })
    ResponseEntity<ResponseMessage> createCustomPackage(CustomPackagePostDto customPackagePostDto, HttpServletRequest request);



    @Operation(summary = "List all Custom Packages", description = "To perform this request you need role ADMIN", tags = "ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Custom package listed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request")
    })
    ResponseEntity<Page<CustomPackageResponse>> findAll(Pageable pageable);

    @Operation(summary = "List all Custom Packages with status pending worker", description = "To perform this request you need role ADMIN or WORKER", tags = {"ADMIN", "WORKER"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Custom package listed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request")
    })
    ResponseEntity<Page<CustomPackageResponse>> findAllCustomPackagePendingWorker(Pageable pageable);

    @Operation(summary = "To perform this request you need role WORKER or ADMIN, USERS does not perform this request", tags = {"ADMIN", "WORKER"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Custom package not found or User not found"),
            @ApiResponse(responseCode = "409", description = "This package has already been accepted by another worker"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request"),
            @ApiResponse(responseCode = "200", description = "Package accept successfully")
    })
    ResponseEntity<ResponseMessage> accept_service(HttpServletRequest request, UUID packageID);


    @Operation(summary = "To perform this request you need role WORKER or ADMIN, USERS does not perform this request", tags = {"ADMIN", "WORKER"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Custom package not found or User not found"),
            @ApiResponse(responseCode = "409", description = "This package has already finished, or Worker not allowed"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request"),
            @ApiResponse(responseCode = "400", description = "User does not have permission to finish a package"),
            @ApiResponse(responseCode = "204", description = "Package finished successfully")
    })
    ResponseEntity<ResponseMessage> finish_service(HttpServletRequest request, UUID packageID);

    @Operation(summary = "To perform this request you need role WORKER or ADMIN, USERS does not perform this request", tags = {"ADMIN", "WORKER"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Custom package not found or User not found"),
            @ApiResponse(responseCode = "409", description = "This package has already closed, or User not allowed"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request"),
            @ApiResponse(responseCode = "400", description = "User does not have permission to close a package"),
            @ApiResponse(responseCode = "204", description = "Package closed and deleted successfully")
    })
    ResponseEntity<ResponseMessage> close_service(HttpServletRequest request, UUID packageID);
}