package com.fredyhg.destiny2jobs.services;

import com.fredyhg.destiny2jobs.exceptions.MissionException;
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
        MissionModel missionToBeSaved = ModelMappers.missionGetDtoToMissionModel(missionPostDto);

        missionRepository.save(missionToBeSaved);
    }

    public Page<MissionGetDto> getAllMissions(Pageable pageable) {

        Page<MissionModel> allMissionModel = missionRepository.findAll(pageable);

        List<MissionGetDto> missionsDtos = allMissionModel.stream()
                .map(ModelMappers::missionModelToMissionGetDto)
                .toList();

        return new PageImpl<>(missionsDtos);
    }

    public void editMission(MissionPostDto missionPostDto) {

        Optional<MissionModel> missionExists = missionExistByName(missionPostDto.getMissionName());

        if(missionExists.isEmpty())
            throw new MissionException("Mission not exists");

        MissionModel missionToBeSaved= ModelMappers.missionPostDtoToMissionModel(missionPostDto);
        missionToBeSaved.setId(missionExists.get().getId());

        missionRepository.save(missionToBeSaved);
    }

    public void deleteMission(MissionDeleteDto missionDeleteDto) {
        Optional<MissionModel> missionExists = missionExistByName(missionDeleteDto.getMissionName());

        if(missionExists.isEmpty())
            throw new MissionException("Mission not exists");

        missionRepository.delete(missionExists.get());
    }



    private Optional<MissionModel> missionExistByName(String mission_name){

        Optional<MissionModel> missionExists = missionRepository.findByMissionName(mission_name);

        if(missionExists.isEmpty())
            throw new MissionException("Mission not exists");

        return missionExists;
    }
}
