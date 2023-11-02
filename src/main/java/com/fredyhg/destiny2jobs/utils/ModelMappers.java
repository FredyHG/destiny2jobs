package com.fredyhg.destiny2jobs.utils;


import com.fredyhg.destiny2jobs.enums.Role;
import com.fredyhg.destiny2jobs.models.MissionModel;
import com.fredyhg.destiny2jobs.models.UserModel;
import com.fredyhg.destiny2jobs.models.dtos.mission.MissionGetDto;
import com.fredyhg.destiny2jobs.models.dtos.mission.MissionPostDto;
import com.fredyhg.destiny2jobs.models.dtos.user.UserGetDto;
import com.fredyhg.destiny2jobs.models.dtos.user.UserPostDto;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ModelMappers {

    public static UserGetDto userModelToUserGetDto(final UserModel userModel){
        return UserGetDto.builder()
                .name(userModel.getName())
                .lastName(userModel.getLastName())
                .email(userModel.getEmail())
                .discordName(userModel.getDiscordName())
                .balance(userModel.getBalance())
                .createAt(userModel.getCreateAt())
                .role(userModel.getRole())
                .build();


    }

    public static MissionModel missionGetDtoToMissionModel(final MissionPostDto missionPostDto){
        return MissionModel.builder()
                .missionName(missionPostDto.getMissionName())
                .estimatedTime(missionPostDto.getEstimatedTime())
                .price(missionPostDto.getPrice())
                .build();
    }

    public static MissionGetDto missionModelToMissionGetDto(final MissionModel missionModel){
        return MissionGetDto.builder()
                .missionName(missionModel.getMissionName())
                .estimated_time(missionModel.getEstimatedTime())
                .price(missionModel.getPrice())
                .build();
    }

    public static MissionModel missionPostDtoToMissionModel(final MissionPostDto missionPostDto){
        return MissionModel.builder()
                .missionName(missionPostDto.getMissionName())
                .estimatedTime(missionPostDto.getEstimatedTime())
                .price(missionPostDto.getPrice())
                .build();
    }


    public static UserModel userPostDtoToUserModel(final UserPostDto user) {
        return UserModel.builder()
                .createAt(new Date())
                .balance(0.0)
                .email(user.getEmail())
                .password(user.getPassword())
                .role(Role.ROLE_USER)
                .name(user.getName())
                .lastName(user.getLastName())
                .discordName(user.getDiscordName())
                .build();
    }



}
