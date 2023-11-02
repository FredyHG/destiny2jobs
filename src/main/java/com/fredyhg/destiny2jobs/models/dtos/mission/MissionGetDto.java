package com.fredyhg.destiny2jobs.models.dtos.mission;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MissionGetDto {

    private String missionName;

    private Integer estimated_time;

    private double price;

}
