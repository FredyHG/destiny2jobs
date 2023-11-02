package com.fredyhg.destiny2jobs.controllers.impls;

import com.fredyhg.destiny2jobs.controllers.CustomPackageController;
import com.fredyhg.destiny2jobs.models.dtos.custompackage.CustomPackagePostDto;
import com.fredyhg.destiny2jobs.models.dtos.custompackage.CustomPackageResponse;
import com.fredyhg.destiny2jobs.services.PackageService;
import com.fredyhg.destiny2jobs.utils.models.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @GetMapping("/all")
    public ResponseEntity<List<CustomPackageResponse>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(packageService.findAllPackage());
    }

    @GetMapping("/wait-worker")
    public ResponseEntity<List<CustomPackageResponse>> findAllCustomPackagePendingWorker(){
        return ResponseEntity.status(HttpStatus.OK).body(packageService.findAllCustomPackagePendingWorker());
    }

    @PostMapping("/accept-service")
    public ResponseEntity<ResponseMessage> accept_service(HttpServletRequest request,
                                                          HttpServletResponse response,
                                                          @RequestParam(name = "packageId")UUID packageID){

        packageService.accept_service(request, response, packageID);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Service accept successfully"));
    }



}
