package com.fredyhg.destiny2jobs.services;

import com.fredyhg.destiny2jobs.enums.Role;
import com.fredyhg.destiny2jobs.exceptions.user.UserException;
import com.fredyhg.destiny2jobs.models.MissionModel;
import com.fredyhg.destiny2jobs.models.UserModel;
import com.fredyhg.destiny2jobs.models.dtos.custompackage.CustomPackagePostDto;
import com.fredyhg.destiny2jobs.models.dtos.mission.MissionsBaseDto;
import com.fredyhg.destiny2jobs.repositories.CustomPackageRepository;
import com.fredyhg.destiny2jobs.repositories.MissionRepository;
import com.fredyhg.destiny2jobs.repositories.PackageRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

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
    void findAll() {
    }

    @Test
    void findAllCustomPackagePendingWorker() {
    }

    @Test
    void accept_service() {
    }

    @Test
    void finish_service() {
    }
}