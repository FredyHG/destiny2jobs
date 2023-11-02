package com.fredyhg.destiny2jobs.models.dtos.mission;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MissionsBaseDto {

    private String missionName;

    private int quantity;

}
