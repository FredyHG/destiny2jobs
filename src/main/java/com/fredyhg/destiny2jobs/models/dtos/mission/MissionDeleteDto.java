package com.fredyhg.destiny2jobs.models.dtos.mission;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MissionDeleteDto {
    @NotBlank
    private String missionName;

}
