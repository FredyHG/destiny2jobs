package com.fredyhg.destiny2jobs.controllers.impls;

import com.fredyhg.destiny2jobs.controllers.CustomPackageController;
import com.fredyhg.destiny2jobs.models.dtos.custompackage.CustomPackagePostDto;
import com.fredyhg.destiny2jobs.services.PackageService;
import com.fredyhg.destiny2jobs.utils.models.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/package")
@RequiredArgsConstructor
public class CustomPackageControllerImpl implements CustomPackageController {

    private final PackageService packageService;

    @PostMapping
    @Override
    public ResponseEntity<ResponseMessage> createCustomPackage(@RequestBody CustomPackagePostDto customPackagePostDto,
                                                               HttpServletRequest request,
                                                               HttpServletResponse response){
        packageService.createCustomPackage(customPackagePostDto, request, response);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("Package created successfully"));
    }


}
