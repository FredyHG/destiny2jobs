package com.fredyhg.destiny2jobs.controllers.impls;

import com.fredyhg.destiny2jobs.controllers.CustomPackageController;
import com.fredyhg.destiny2jobs.models.dtos.custompackage.CustomPackagePostDto;
import com.fredyhg.destiny2jobs.models.dtos.custompackage.CustomPackageResponse;
import com.fredyhg.destiny2jobs.services.CustomPackageService;
import com.fredyhg.destiny2jobs.utils.models.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
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

    private final CustomPackageService customPackageService;

    @PostMapping
    @Override
    public ResponseEntity<ResponseMessage> createCustomPackage(@RequestBody CustomPackagePostDto customPackagePostDto,
                                                               HttpServletRequest request){
        customPackageService.createCustomPackage(customPackagePostDto, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("Package created successfully"));
    }

    @GetMapping("/all")
    @Override
    public ResponseEntity<List<CustomPackageResponse>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(customPackageService.findAllPackage());
    }

    @GetMapping("/wait-worker")
    @Override
    public ResponseEntity<List<CustomPackageResponse>> findAllCustomPackagePendingWorker(){
        return ResponseEntity.status(HttpStatus.OK).body(customPackageService.findAllCustomPackagePendingWorker());
    }

    @PostMapping("/accept-service")
    @Override
    public ResponseEntity<ResponseMessage> accept_service(HttpServletRequest request,
                                                          @RequestParam(name = "packageId")UUID packageID){

        customPackageService.accept_service(request, packageID);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Service accept successfully"));
    }

    @PostMapping("/finish-service")
    @Override
    public ResponseEntity<ResponseMessage> finish_service(HttpServletRequest request,
                                                          @RequestParam(name = "packageId")UUID packageID){

        customPackageService.finish_service(request, packageID);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseMessage("Package finished successfully"));
    }

    @DeleteMapping("/close-service")
    @Override
    public ResponseEntity<ResponseMessage> close_service(HttpServletRequest request,
                                                         @RequestParam(name = "packageId")UUID packageID){

        customPackageService.close_service(request, packageID);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("This package deleted successfully and deleted"));
    }



}
