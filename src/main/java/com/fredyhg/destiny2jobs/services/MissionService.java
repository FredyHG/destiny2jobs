package com.fredyhg.destiny2jobs.services;

import com.fredyhg.destiny2jobs.exceptions.mission.MissionAlreadyException;
import com.fredyhg.destiny2jobs.exceptions.mission.MissionNotFoundException;
import com.fredyhg.destiny2jobs.models.MissionModel;
import com.fredyhg.destiny2jobs.models.dtos.mission.MissionDeleteDto;
import com.fredyhg.destiny2jobs.models.dtos.mission.MissionGetDto;
import com.fredyhg.destiny2jobs.models.dtos.mission.MissionPostDto;
import com.fredyhg.destiny2jobs.repositories.MissionRepository;
import com.fredyhg.destiny2jobs.utils.ModelMappers;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;

    public void createMission(MissionPostDto missionPostDto){

       ensureMissionNonExists(missionPostDto.getMissionName());

        MissionModel missionToBeSaved = ModelMappers.missionGetDtoToMissionModel(missionPostDto);

        missionRepository.save(missionToBeSaved);
    }

    public Page<MissionGetDto> getAllMissions(Pageable pageable) {

        Page<MissionModel> allMissionModel = missionRepository.findAll(pageable);

        List<MissionGetDto> listOfMissionsDto = allMissionModel.stream()
                .map(ModelMappers::missionModelToMissionGetDto)
                .toList();

        return new PageImpl<>(listOfMissionsDto);
    }

    public void editMission(MissionPostDto missionPostDto) {

        MissionModel missionModel = ensureMissionExistsByName(missionPostDto.getMissionName());

        MissionModel missionToBeSaved= ModelMappers.missionPostDtoToMissionModel(missionPostDto);
        missionToBeSaved.setId(missionModel.getId());

        missionRepository.save(missionToBeSaved);
    }

    public void deleteMission(MissionDeleteDto missionDeleteDto) {

        MissionModel missionModel = ensureMissionExistsByName(missionDeleteDto.getMissionName());

        missionRepository.delete(missionModel);
    }


    public MissionModel ensureMissionExistsByName(String missionName) {

        Optional<MissionModel> missionExists = missionRepository.findByMissionName(missionName);

        if (missionExists.isEmpty())
            throw new MissionNotFoundException("Mission not exists");

        return missionExists.get();
    }

    public void ensureMissionNonExists(String missionName){

        Optional<MissionModel> missionExists = missionRepository.findByMissionName(missionName);

        if(missionExists.isPresent())
            throw new MissionAlreadyException("Mission already exists");
    }
}
