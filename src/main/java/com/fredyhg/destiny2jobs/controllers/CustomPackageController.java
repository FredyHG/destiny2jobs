package com.fredyhg.destiny2jobs.controllers;

import com.fredyhg.destiny2jobs.models.dtos.custompackage.CustomPackagePostDto;
import com.fredyhg.destiny2jobs.utils.models.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface CustomPackageController {

    ResponseEntity<ResponseMessage> createCustomPackage(CustomPackagePostDto customPackagePostDto, HttpServletRequest request, HttpServletResponse response);

}
