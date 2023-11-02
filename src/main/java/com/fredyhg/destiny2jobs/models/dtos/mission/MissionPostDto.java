package com.fredyhg.destiny2jobs.models.dtos.mission;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MissionPostDto {


    @NotBlank
    private String missionName;

    @Min(2)
    @Max(604800)
    private Integer estimatedTime;

    @Min(1)
    private double price;
}
