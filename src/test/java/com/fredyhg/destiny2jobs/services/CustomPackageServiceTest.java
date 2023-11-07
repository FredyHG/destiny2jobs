package com.fredyhg.destiny2jobs.services;

import com.fredyhg.destiny2jobs.enums.Role;
import com.fredyhg.destiny2jobs.enums.ServiceStatus;
import com.fredyhg.destiny2jobs.exceptions.user.UserException;
import com.fredyhg.destiny2jobs.models.CustomPackageModel;
import com.fredyhg.destiny2jobs.models.MissionModel;
import com.fredyhg.destiny2jobs.models.UserModel;
import com.fredyhg.destiny2jobs.models.dtos.custompackage.CustomPackagePostDto;
import com.fredyhg.destiny2jobs.models.dtos.custompackage.CustomPackageResponse;
import com.fredyhg.destiny2jobs.models.dtos.mission.MissionsBaseDto;
import com.fredyhg.destiny2jobs.repositories.CustomPackageRepository;
import com.fredyhg.destiny2jobs.repositories.MissionRepository;
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
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serial;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    public void testCreateCustomPackageWithWorkerRole() throws Exception {
        CustomPackagePostDto customPackagePostDto = new CustomPackagePostDto();
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
    void accept_service() {
    }

    @Test
    void finish_service() {
    }
}