package com.fredyhg.destiny2jobs.services;

import com.fredyhg.destiny2jobs.enums.Role;
import com.fredyhg.destiny2jobs.enums.ServiceStatus;
import com.fredyhg.destiny2jobs.exceptions.customPackage.CustomPackageException;
import com.fredyhg.destiny2jobs.exceptions.customPackage.CustomPackageNotFoundException;
import com.fredyhg.destiny2jobs.exceptions.mission.CustomPackageAlreadyAccepted;
import com.fredyhg.destiny2jobs.exceptions.user.UserException;
import com.fredyhg.destiny2jobs.exceptions.user.UserNotAllowedException;
import com.fredyhg.destiny2jobs.exceptions.user.WorkerNotAllowedException;
import com.fredyhg.destiny2jobs.models.CustomPackageModel;
import com.fredyhg.destiny2jobs.models.MissionModel;
import com.fredyhg.destiny2jobs.models.UserModel;
import com.fredyhg.destiny2jobs.models.dtos.custompackage.CustomPackagePostDto;
import com.fredyhg.destiny2jobs.models.dtos.custompackage.CustomPackageResponse;
import com.fredyhg.destiny2jobs.repositories.CustomPackageRepository;
import com.fredyhg.destiny2jobs.repositories.MissionRepository;
import com.fredyhg.destiny2jobs.utils.ModelMappers;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomPackageService {


    private final MissionRepository missionRepository;

    private final CustomPackageRepository customPackageRepository;

    private final UserService userService;



    public List<CustomPackageResponse> findAllPackage(){
        List<CustomPackageModel> packageModel = customPackageRepository.findAll();

        return packageModel
                .stream()
                .map(ModelMappers::customPackageModelToCustomPackageResponse)
                .toList();
    }

    public List<CustomPackageResponse> findAllCustomPackagePendingWorker() {
        List<CustomPackageModel> packageModel = customPackageRepository.findAllWhereStatusWaitForWorker();

        return packageModel
                .stream()
                .map(ModelMappers::customPackageModelToCustomPackageResponse)
                .toList();
    }


    public void createCustomPackage(CustomPackagePostDto customPackagePostDtos, HttpServletRequest request) {

        UserModel user = userService.userExistsByToken(request);

        Optional<CustomPackageModel> userHasPackagePending = customPackageRepository.findPendingPackage(user);

        if(userHasPackagePending.isPresent())
            throw new CustomPackageException("User already has a pending package");


        if(user.getRole() == Role.ROLE_WORKER)
            throw new UserException("Worker cannot create a package.");


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

        customPackageRepository.save(customPackageToBeSaved);
    }

    public void accept_service(HttpServletRequest request, UUID packageId) {

        Optional<CustomPackageModel> packageExist = customPackageRepository.findById(packageId);

        if(packageExist.isEmpty())
            throw new CustomPackageNotFoundException("Custom Package not found");

        if(packageExist.get().getWorker() != null)
            throw new CustomPackageAlreadyAccepted("This package has already been accepted");


        UserModel worker = userService.userExistsByToken(request);

        packageExist.get().setWorker(worker);
        packageExist.get().setStatus(ServiceStatus.STARTED);

        customPackageRepository.save(packageExist.get());
    }

    public void finish_service(HttpServletRequest request, UUID packageID) {

        Optional<CustomPackageModel> packageExist = customPackageRepository.findById(packageID);

        if(packageExist.isEmpty())
            throw new CustomPackageNotFoundException("Custom Package not found");


        UserModel worker = userService.userExistsByToken(request);

        if(worker.getRole() == Role.ROLE_USER)
            throw new CustomPackageException("User does not have permission to close a package");

        if(packageExist.get().getWorker() != worker)
            throw new WorkerNotAllowedException("You are not allowed to finish someone else's package");

        packageExist.get().setStatus(ServiceStatus.FINISH);

        customPackageRepository.save(packageExist.get());

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

    public void close_service(HttpServletRequest request, UUID packageID) {

        Optional<CustomPackageModel> packageExist = customPackageRepository.findById(packageID);

        if(packageExist.isEmpty())
            throw new CustomPackageNotFoundException("Custom Package not found");

        UserModel user = userService.userExistsByToken(request);

        if(user.getRole() == Role.ROLE_WORKER)
            throw new CustomPackageException("User does not have permission to close a package");

        if(!packageExist.get().getUser().equals(user))
            throw new UserNotAllowedException("You can't close someone else's package");

        customPackageRepository.delete(packageExist.get());
    }
}
