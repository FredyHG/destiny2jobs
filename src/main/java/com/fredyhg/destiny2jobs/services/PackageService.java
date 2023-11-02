package com.fredyhg.destiny2jobs.services;

import com.fredyhg.destiny2jobs.enums.Role;
import com.fredyhg.destiny2jobs.enums.ServiceStatus;
import com.fredyhg.destiny2jobs.exceptions.UserException;
import com.fredyhg.destiny2jobs.models.MissionModel;
import com.fredyhg.destiny2jobs.models.CustomPackageModel;
import com.fredyhg.destiny2jobs.models.UserModel;
import com.fredyhg.destiny2jobs.models.dtos.custompackage.CustomPackagePostDto;
import com.fredyhg.destiny2jobs.models.dtos.user.UserGetDto;
import com.fredyhg.destiny2jobs.repositories.MissionRepository;
import com.fredyhg.destiny2jobs.repositories.PackageRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PackageService {


    private final MissionRepository missionRepository;

    private final PackageRepository packageRepository;

    private final UserService userService;

    public void createCustomPackage(CustomPackagePostDto customPackagePostDtos, HttpServletRequest request, HttpServletResponse response) {

        UserModel user = userService.userExistsByToken(request);

        if(user.getRole() == Role.ROLE_WORKER){
            throw new UserException("Worker cannot create a package.");
        }

        List<MissionModel> listOfMissions = customPackagePostDtos.getMissions()
                .stream()
                .map(missions -> missionRepository.findByMissionName(missions.getMissionName()).orElseThrow())
                .toList();

        CustomPackageModel customPackageToBeSaved = CustomPackageModel.builder()
                .missions(listOfMissions)
                .price(calcTotalPriceMission(customPackagePostDtos))
                .user(user)
                .status(ServiceStatus.WAIT_FOR_WORKER)
                .estimatedTime(calTotalTimeMission(customPackagePostDtos))
                .build();

        packageRepository.save(customPackageToBeSaved);
    }

    public double calcTotalPriceMission(CustomPackagePostDto customPackagePostDto){

        Map<String, Double> missionPriceMap = missionRepository.findAll()
                .stream()
                .collect(Collectors.toMap(MissionModel::getMissionName, MissionModel::getPrice));

        return customPackagePostDto.getMissions().stream()
                .mapToDouble(missionDto -> {
                    String missionName = missionDto.getMissionName();
                    int quantity = missionDto.getQuantity();

                    double missionPrice = missionPriceMap.getOrDefault(missionName, 0.0);

                    return missionPrice * quantity;
                })
                .sum();
    }

    public int calTotalTimeMission(CustomPackagePostDto customPackagePostDto){
        Map<String, Integer> missionIntMap = missionRepository.findAll()
                .stream()
                .collect(Collectors.toMap(MissionModel::getMissionName, MissionModel::getEstimatedTime));

        return customPackagePostDto.getMissions().stream()
                .mapToInt(missionDto -> {
                    String missionName = missionDto.getMissionName();
                    int quantity = missionDto.getQuantity();

                    int missionTime = missionIntMap.getOrDefault(missionName, 0);

                    return missionTime * quantity;
                })
                .sum();
    }


}
