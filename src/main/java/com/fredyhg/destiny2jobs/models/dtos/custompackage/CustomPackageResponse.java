package com.fredyhg.destiny2jobs.models.dtos.custompackage;

import com.fredyhg.destiny2jobs.enums.ServiceStatus;
import com.fredyhg.destiny2jobs.models.MissionModel;
import com.fredyhg.destiny2jobs.models.dtos.user.UserResponse;
import com.fredyhg.destiny2jobs.models.dtos.user.WorkerResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CustomPackageResponse {


    private UUID id;

    private List<MissionModel> missions;

    private int estimatedTime;

    private double price;

    private UserResponse user;

    private WorkerResponse worker;

    private ServiceStatus status;

}
