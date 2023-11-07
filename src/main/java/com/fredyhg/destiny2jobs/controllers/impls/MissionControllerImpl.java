package com.fredyhg.destiny2jobs.controllers.impls;

import com.fredyhg.destiny2jobs.controllers.MissionController;
import com.fredyhg.destiny2jobs.models.dtos.mission.MissionDeleteDto;
import com.fredyhg.destiny2jobs.models.dtos.mission.MissionGetDto;
import com.fredyhg.destiny2jobs.models.dtos.mission.MissionPostDto;
import com.fredyhg.destiny2jobs.services.MissionService;
import com.fredyhg.destiny2jobs.utils.models.ResponseMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/mission")
@RequiredArgsConstructor
public class MissionControllerImpl implements MissionController {

    private final MissionService missionService;

    @PostMapping("/create")
    @Override
    public ResponseEntity<ResponseMessage> createMission(@Valid @RequestBody MissionPostDto missionPostDto){
        missionService.createMission(missionPostDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("Mission created successfully"));
    }

    @GetMapping("/all")
    @Override
    public ResponseEntity<Page<MissionGetDto>> getAllMissions(Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(missionService.getAllMissions(pageable));
    }


    @PostMapping("/edit")
    @Override
    public ResponseEntity<ResponseMessage> editMission(@Valid @RequestBody MissionPostDto missionPostDto){
        missionService.editMission(missionPostDto);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Mission edited successfully"));
    }

    @DeleteMapping("/delete")
    @Override
    public ResponseEntity<ResponseMessage> deleteMission(@RequestBody MissionDeleteDto missionDeleteDto){
        missionService.deleteMission(missionDeleteDto);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Mission deleted successfully"));
    }
}
