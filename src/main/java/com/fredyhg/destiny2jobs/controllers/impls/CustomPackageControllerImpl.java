package com.fredyhg.destiny2jobs.controllers.impls;

import com.fredyhg.destiny2jobs.controllers.CustomPackageController;
import com.fredyhg.destiny2jobs.models.dtos.custompackage.CustomPackagePostDto;
import com.fredyhg.destiny2jobs.models.dtos.custompackage.CustomPackageResponse;
import com.fredyhg.destiny2jobs.services.CustomPackageService;
import com.fredyhg.destiny2jobs.utils.models.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("api/package")
@RequiredArgsConstructor
public class CustomPackageControllerImpl implements CustomPackageController {

    private final CustomPackageService customPackageService;

    @PostMapping("/create")
    @Override
    public ResponseEntity<ResponseMessage> createCustomPackage(@RequestBody CustomPackagePostDto customPackagePostDto,
                                                               HttpServletRequest request){

        customPackageService.createCustomPackage(customPackagePostDto, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("Package created successfully"));
    }

    @GetMapping("/all")
    @Override
    public ResponseEntity<Page<CustomPackageResponse>> findAll(Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(customPackageService.findAllPackage(pageable));
    }

    @GetMapping("/wait-worker")
    @Override
    public ResponseEntity<Page<CustomPackageResponse>> findAllCustomPackagePendingWorker(Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(customPackageService.findAllCustomPackagePendingWorker(pageable));
    }

    @PostMapping("/accept-service")
    @Override
    public ResponseEntity<ResponseMessage> accept_service(HttpServletRequest request,
                                                          @RequestParam(name = "packageId")UUID packageID){

        customPackageService.acceptService(request, packageID);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Service accept successfully"));
    }

    @PostMapping("/finish-service")
    @Override
    public ResponseEntity<ResponseMessage> finish_service(HttpServletRequest request,
                                                          @RequestParam(name = "packageId")UUID packageID){

        customPackageService.finishService(request, packageID);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseMessage("Package finished successfully"));
    }

    @PostMapping("/close-service")
    @Override
    public ResponseEntity<ResponseMessage> close_service(HttpServletRequest request,
                                                         @RequestParam(name = "packageId")UUID packageID){

        customPackageService.closeService(request, packageID);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("This package deleted successfully and deleted"));
    }



}
