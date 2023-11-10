package com.fredyhg.destiny2jobs.services;

import com.fredyhg.destiny2jobs.enums.Role;
import com.fredyhg.destiny2jobs.enums.ServiceStatus;
import com.fredyhg.destiny2jobs.exceptions.customPackage.CustomPackageException;
import com.fredyhg.destiny2jobs.exceptions.mission.CustomPackageAlreadyAccepted;
import com.fredyhg.destiny2jobs.exceptions.user.UserException;
import com.fredyhg.destiny2jobs.models.CustomPackageModel;
import com.fredyhg.destiny2jobs.models.MissionModel;
import com.fredyhg.destiny2jobs.models.UserModel;
import com.fredyhg.destiny2jobs.models.dtos.custompackage.CustomPackagePostDto;
import com.fredyhg.destiny2jobs.models.dtos.custompackage.CustomPackageResponse;
import com.fredyhg.destiny2jobs.models.dtos.mission.MissionsBaseDto;
import com.fredyhg.destiny2jobs.repositories.CustomPackageRepository;
import com.fredyhg.destiny2jobs.repositories.MissionRepository;
import com.fredyhg.destiny2jobs.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomPackageServiceTest {

    @InjectMocks
    private CustomPackageService customPackageService;

    @Mock
    private UserService userService;

    @Mock
    private CustomPackageRepository customPackageRepository;

    @Mock
    private MissionRepository missionRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testCreateCustomPackage() {

        MissionsBaseDto missionMock = MissionsBaseDto.builder()
                .missionName("Mission mock")
                .quantity(1)
                .build();

        CustomPackagePostDto customPackagePostDto = new CustomPackagePostDto();
        customPackagePostDto.setMissions(Collections.singletonList(missionMock));

        HttpServletRequest request = mock(HttpServletRequest.class);
        UserModel user = new UserModel();
        user.setRole(Role.ROLE_USER);
        when(userService.userExistsByToken(request)).thenReturn(user);
        when(missionRepository.findByMissionName(anyString())).thenReturn(Optional.of(new MissionModel()));

        customPackageService.createCustomPackage(customPackagePostDto, request);


        verify(customPackageRepository, times(1)).save(any());
    }

    @Test
    public void testCreateCustomPackageWithWorkerRole() {
        MissionsBaseDto missions = MissionsBaseDto.builder()
                .missionName("mockMission")
                .quantity(1)
                .build();

        CustomPackagePostDto customPackagePostDto = new CustomPackagePostDto();

        customPackagePostDto.setMissions(List.of(missions));
        HttpServletRequest request = mock(HttpServletRequest.class);
        UserModel user = new UserModel();
        user.setRole(Role.ROLE_WORKER);


        when(userService.userExistsByToken(request)).thenReturn(user);

        assertThrows(UserException.class, () -> customPackageService.createCustomPackage(customPackagePostDto, request));
        verify(customPackageRepository, never()).save(any());
    }

    @Test
    public void testFindAllPackage() {
        CustomPackageModel mockPackage1 = CustomPackageModel.builder()
                .user(new UserModel())
                .missions(Collections.emptyList())
                .status(ServiceStatus.STARTED)
                .price(2.0)
                .worker(new UserModel())
                .estimatedTime(2323)
                .build();

        CustomPackageModel mockPackage2 = CustomPackageModel.builder()
                .user(new UserModel())
                .missions(Collections.emptyList())
                .status(ServiceStatus.STARTED)
                .price(2.0)
                .worker(new UserModel())
                .estimatedTime(2323)
                .build();

        List<CustomPackageModel> mockPackageList = Arrays.asList(mockPackage1, mockPackage2);
        Page<CustomPackageModel> mockPackagePage = new PageImpl<>(mockPackageList);

        when(customPackageRepository.findAll(PageRequest.of(0, 1))).thenReturn(mockPackagePage);

        Pageable pageable = PageRequest.of(0, 1);
        Page<CustomPackageResponse> customPackages = customPackageService.findAllPackage(pageable);

        assertEquals(2, customPackages.getTotalElements());
    }


    @Test
    public void testFindAllCustomPackagePendingWorker() {
        CustomPackageModel mockPackage1 = CustomPackageModel.builder()
                .user(new UserModel())
                .missions(Collections.emptyList())
                .status(ServiceStatus.WAIT_FOR_WORKER)
                .price(2.0)
                .worker(new UserModel())
                .estimatedTime(2323)
                .build();

        CustomPackageModel mockPackage2 = CustomPackageModel.builder()
                .user(new UserModel())
                .missions(Collections.emptyList())
                .status(ServiceStatus.WAIT_FOR_WORKER)
                .price(2.0)
                .worker(new UserModel())
                .estimatedTime(2323)
                .build();


        List<CustomPackageModel> mockPackageList = Arrays.asList(mockPackage1, mockPackage2);
        Page<CustomPackageModel> mockPackagePage = new PageImpl<>(mockPackageList);

        when(customPackageRepository.findAllWhereStatusWaitForWorker(PageRequest.of(0, 1))).thenReturn(mockPackagePage);

        Pageable pageable = PageRequest.of(0, 1);
        Page<CustomPackageResponse> customPackages = customPackageService.findAllCustomPackagePendingWorker(pageable);

        assertEquals(2, customPackages.getTotalElements());
    }


    @Test
    public void testFinishService() {

        UserModel mockWorker = UserModel.builder()
                .role(Role.ROLE_WORKER)
                .balance(0.0)
                .build();

        UUID packageId = UUID.randomUUID();
        HttpServletRequest request = mock(HttpServletRequest.class);
        CustomPackageModel customPackageModel = new CustomPackageModel();
        customPackageModel.setPrice(2.0);
        customPackageModel.setStatus(ServiceStatus.STARTED);
        customPackageModel.setWorker(mockWorker);

        when(customPackageRepository.findById(packageId)).thenReturn(Optional.of(customPackageModel));
        when(userService.userExistsByToken(request)).thenReturn(mockWorker);

        customPackageService.finishService(request, packageId);

        verify(customPackageRepository, times(1)).save(customPackageModel);
        assertEquals(ServiceStatus.FINISH, customPackageModel.getStatus());
    }

    @Test
    public void testFinishServiceInvalidStatus() {

        UserModel mockUser = UserModel.builder()
                .role(Role.ROLE_WORKER)
                .build();

        UUID packageId = UUID.randomUUID();
        HttpServletRequest request = mock(HttpServletRequest.class);
        CustomPackageModel customPackageModel = new CustomPackageModel();
        customPackageModel.setStatus(ServiceStatus.WAIT_FOR_WORKER);
        customPackageModel.setWorker(mockUser);
        customPackageModel.setPrice(2.0);

        when(customPackageRepository.findById(packageId)).thenReturn(Optional.of(customPackageModel));
        when(userService.userExistsByToken(request)).thenReturn(mockUser);

        assertThrows(CustomPackageException.class, () -> customPackageService.finishService(request, packageId));
        verify(customPackageRepository, never()).save(customPackageModel);
    }

    @Test
    public void testFinishServiceInvalidWorker() {

        UserModel mockUser = UserModel.builder()
                .role(Role.ROLE_WORKER)
                .build();

        UUID packageId = UUID.randomUUID();
        HttpServletRequest request = mock(HttpServletRequest.class);
        CustomPackageModel customPackageModel = new CustomPackageModel();
        customPackageModel.setStatus(ServiceStatus.WAIT_FOR_WORKER);
        customPackageModel.setWorker(mockUser);


        when(customPackageRepository.findById(packageId)).thenReturn(Optional.of(customPackageModel));
        when(userService.userExistsByToken(request)).thenReturn(mockUser);

        assertThrows(CustomPackageException.class, () -> customPackageService.finishService(request, packageId));
        verify(customPackageRepository, never()).save(customPackageModel);
    }
    @Test
    public void testAcceptService() {
        UUID packageId = UUID.randomUUID();
        HttpServletRequest request = mock(HttpServletRequest.class);
        CustomPackageModel customPackageModel = new CustomPackageModel();
        customPackageModel.setStatus(ServiceStatus.WAIT_FOR_WORKER);
        when(customPackageRepository.findById(packageId)).thenReturn(Optional.of(customPackageModel));
        when(userService.userExistsByToken(request)).thenReturn(new UserModel());

        customPackageService.acceptService(request, packageId);

        verify(customPackageRepository, times(1)).save(customPackageModel);
        assertEquals(ServiceStatus.STARTED, customPackageModel.getStatus());
        assertNotNull(customPackageModel.getWorker());
    }

    @Test
    public void testAcceptServiceAlreadyAccepted() {
        UUID packageId = UUID.randomUUID();
        HttpServletRequest request = mock(HttpServletRequest.class);
        CustomPackageModel customPackageModel = new CustomPackageModel();
        customPackageModel.setWorker(new UserModel());
        when(customPackageRepository.findById(packageId)).thenReturn(Optional.of(customPackageModel));

        assertThrows(CustomPackageAlreadyAccepted.class, () -> customPackageService.acceptService(request, packageId));
        verify(customPackageRepository, never()).save(customPackageModel);
    }

}