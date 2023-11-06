package com.fredyhg.destiny2jobs.controllers;

import com.fredyhg.destiny2jobs.models.dtos.custompackage.CustomPackagePostDto;
import com.fredyhg.destiny2jobs.models.dtos.custompackage.CustomPackageResponse;
import com.fredyhg.destiny2jobs.utils.models.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;


public interface CustomPackageController {


    @Operation(summary = "Create new account", description = "To perform this request you need role ADMIN or USER, WORKERS does not perform this request", tags = {"USER", "ADMIN"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Custom Package created successfully"),
            @ApiResponse(responseCode = "409", description = "Package already exists"),
            @ApiResponse(responseCode = "400", description = "Worker does not allowed to perform this request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request")
    })
    ResponseEntity<ResponseMessage> createCustomPackage(CustomPackagePostDto customPackagePostDto, HttpServletRequest request);

    ResponseEntity<List<CustomPackageResponse>> findAll();

    ResponseEntity<List<CustomPackageResponse>> findAllCustomPackagePendingWorker();

    ResponseEntity<ResponseMessage> accept_service(HttpServletRequest request, UUID packageID);

    ResponseEntity<ResponseMessage> finish_service(HttpServletRequest request, UUID packageID);

    ResponseEntity<ResponseMessage> close_service(HttpServletRequest request, UUID packageID);
}