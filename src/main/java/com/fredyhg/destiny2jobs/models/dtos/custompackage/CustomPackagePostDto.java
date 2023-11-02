package com.fredyhg.destiny2jobs.models.dtos.custompackage;

import com.fredyhg.destiny2jobs.models.dtos.mission.MissionsBaseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomPackagePostDto {
    List<MissionsBaseDto> missions;
}
