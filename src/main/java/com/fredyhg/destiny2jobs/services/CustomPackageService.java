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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    public Page<CustomPackageResponse> findAllPackage(Pageable pageable){
        Page<CustomPackageModel> packageModel = customPackageRepository.findAll(pageable);

        List<CustomPackageResponse> listOfPackages = packageModel
                .stream()
                .map(ModelMappers::customPackageModelToCustomPackageResponse)
                .toList();

        return new PageImpl<>(listOfPackages);
    }

    public Page<CustomPackageResponse> findAllCustomPackagePendingWorker(Pageable pageable) {
        Page<CustomPackageModel> packageModel = customPackageRepository.findAllWhereStatusWaitForWorker(pageable);

        List<CustomPackageResponse> listOfPackages = packageModel
                .stream()
                .map(ModelMappers::customPackageModelToCustomPackageResponse)
                .toList();

        return new PageImpl<>(listOfPackages);
    }

    public void createCustomPackage(CustomPackagePostDto customPackagePostDtos, HttpServletRequest request) {

        UserModel user = userService.userExistsByToken(request);

        Optional<CustomPackageModel> userHasPackagePending = customPackageRepository.findPendingPackage(user);

        double calcTotalPriceMission = calcTotalPriceMission(customPackagePostDtos);

        userService.debitBalance(user, calcTotalPriceMission);

        if(userHasPackagePending.isPresent()) throw new CustomPackageException("User already has a pending package");

        if(user.getRole() == Role.ROLE_WORKER) throw new UserException("Worker cannot create a package.");


        List<MissionModel> listOfMissions = customPackagePostDtos.getMissions()
                .stream()
                .map(missions -> missionRepository.findByMissionName(missions.getMissionName()).orElseThrow())
                .toList();

        CustomPackageModel customPackageToBeSaved = CustomPackageModel.builder()
                .missions(listOfMissions)
                .price(calcTotalPriceMission)
                .user(user)
                .status(ServiceStatus.WAIT_FOR_WORKER)
                .estimatedTime(calTotalTimeMission(customPackagePostDtos))
                .build();

        customPackageRepository.save(customPackageToBeSaved);
    }

    public void accept_service(HttpServletRequest request, UUID packageId) {

        CustomPackageModel customPackageModel = ensureCustomPackageModelExistsById(packageId);

        if(customPackageModel.getWorker() != null) throw new CustomPackageAlreadyAccepted("This package has already been accepted");

        UserModel worker = userService.userExistsByToken(request);

        customPackageModel.setWorker(worker);
        customPackageModel.setStatus(ServiceStatus.STARTED);

        customPackageRepository.save(customPackageModel);
    }

    public void finish_service(HttpServletRequest request, UUID packageID) {

        CustomPackageModel customPackageModel = ensureCustomPackageModelExistsById(packageID);

        UserModel worker = userService.userExistsByToken(request);

        if(!worker.hasPermissionToClosePackage(worker)) throw new CustomPackageException("User does not have permission to close a package");

        if(!customPackageModel.getWorker().isWorkerAllowedToFinish(worker)) throw new WorkerNotAllowedException("You are not allowed to finish someone else's package");

        if(!customPackageModel.isValidStatusForFinishing(customPackageModel)) throw new CustomPackageException("Invalid package status for finishing the service");

        customPackageModel.setStatus(ServiceStatus.FINISH);

        customPackageRepository.save(customPackageModel);

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

        CustomPackageModel customPackageModel = ensureCustomPackageModelExistsById(packageID);

        UserModel user = userService.userExistsByToken(request);

        if(user.getRole() == Role.ROLE_WORKER) throw new CustomPackageException("User does not have permission to close a package");

        if(!customPackageModel.getUser().equals(user)) throw new UserNotAllowedException("You can't close someone else's package");

        customPackageRepository.delete(customPackageModel);
    }

    public CustomPackageModel ensureCustomPackageModelExistsById(UUID id ){

        Optional<CustomPackageModel> packageExist = customPackageRepository.findById(id);

        if(packageExist.isEmpty()) throw new CustomPackageNotFoundException("Custom Package not found");

        return packageExist.get();
    }


}
