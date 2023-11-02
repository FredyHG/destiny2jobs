package com.fredyhg.destiny2jobs.controllers;

import com.fredyhg.destiny2jobs.models.dtos.mission.MissionDeleteDto;
import com.fredyhg.destiny2jobs.models.dtos.mission.MissionGetDto;
import com.fredyhg.destiny2jobs.models.dtos.mission.MissionPostDto;
import com.fredyhg.destiny2jobs.utils.models.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface MissionController {

    @Operation(summary = "Create new mission", description = "To perform the request u need ADMIN role", tags = "ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Mission created successfully"),
            @ApiResponse(responseCode = "400", description = "Mission already exists"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request")
    })
    ResponseEntity<ResponseMessage> createMission(MissionPostDto missionPostDto);

    @Operation(summary = "List all missions", description = "All can make this request.", tags = "ALL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all mission successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request")
    })
    ResponseEntity<Page<MissionGetDto>> getAllMissions(Pageable pageable);

    @Operation(summary = "Edit mission infos", description = "To perform the request u need ADMIN role", tags = "ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mission edited successfully"),
            @ApiResponse(responseCode = "400", description = "Mission not exists"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request")
    })
    ResponseEntity<ResponseMessage> editMission(MissionPostDto missionPostDto);

    @Operation(summary = "List all users", description = "To perform the request u need ADMIN role.", tags = "ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mission deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Mission not exists"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request")
    })
    ResponseEntity<ResponseMessage> deleteMission(MissionDeleteDto missionDeleteDto);
}
