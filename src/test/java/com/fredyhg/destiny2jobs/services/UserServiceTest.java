package com.fredyhg.destiny2jobs.services;

import com.fredyhg.destiny2jobs.exceptions.UserException;
import com.fredyhg.destiny2jobs.models.UserModel;
import com.fredyhg.destiny2jobs.models.dtos.user.UserGetDto;
import com.fredyhg.destiny2jobs.models.dtos.user.UserPostDto;
import com.fredyhg.destiny2jobs.repositories.UserRepository;
import com.fredyhg.destiny2jobs.security.token.TokenRepository;
import com.fredyhg.destiny2jobs.utils.ModelMappers;
import com.fredyhg.destiny2jobs.utils.UserCreator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private JwtService jwtService;


    @Test
    public void createUser_EmailAlreadyExists_ThrowsUserException() {

        UserPostDto userDto = new UserPostDto();
        userDto.setEmail("test@example.com");
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(new UserModel()));


        assertThrows(UserException.class, () -> {
            userService.createUser(userDto);
        });
    }

    @Test
    public void createUser_ValidUser_SavesUser() {

        UserPostDto userDto = new UserPostDto();
        userDto.setEmail("test@example.com");
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());

        userService.createUser(userDto);

        verify(userRepository, times(1)).save(any(UserModel.class));
    }

    @Test
    public void testListAllUsers() {
        Pageable pageable = Pageable.unpaged();

        List<UserModel> userModelList = new ArrayList<>();
        userModelList.add(UserCreator.createValidUserModel());

        Page<UserModel> userModelPage = new PageImpl<>(userModelList, pageable, userModelList.size());

        when(userRepository.findAll(pageable)).thenReturn(userModelPage);

        Page<UserGetDto> resultPage = userService.listAllUsers(pageable);

        assertEquals(userModelList.size(), resultPage.getContent().size());
    }

    @Test
    public void testDeleteById_WhenIdExists() {
        UUID uuid = UUID.randomUUID();

        when(userRepository.findById(uuid)).thenReturn(Optional.of(UserCreator.createValidUserModel()));

        userService.deleteById(uuid);

        verify(userRepository, times(1)).deleteById(uuid);
    }

    @Test
    public void testDeleteById_WhenIdDoesNotExists() {
        UUID uuid = UUID.randomUUID();

        when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> userService.deleteById(uuid));
    }

    @Test
    public void testGetCurrentUserData() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        String mockToken = "Bearer mockToken";
        String mockEmail = "user@example.com";

        UserModel mockUserModel = UserCreator.createValidUserModel();
        UserGetDto mockUserGetDto = ModelMappers.userModelToUserGetDto(mockUserModel);

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(mockToken);

        when(jwtService.extractUsername(mockToken.substring(7))).thenReturn(mockEmail);

        when(userRepository.findByEmail(mockEmail)).thenReturn(Optional.of(mockUserModel));

        UserGetDto result = userService.getCurrentUserData(request, response);

        assertEquals(mockUserGetDto, result);
    }

    @Test
    public void testIsEmailAvailable_WhenEmailIsAvailable() {
        String email = "new@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        userService.isEmailAvailable(email);
    }

    @Test
    public void testIsEmailAvailable_WhenEmailIsNotAvailable() {
        String email = "existing@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(UserCreator.createValidUserModel()));

        UserException exception = assertThrows(UserException.class, () -> userService.isEmailAvailable(email));
        assertEquals("Email already registered", exception.getMessage());
    }

    @Test
    public void testIsDiscordNameAvailable_WhenEmailIsAvailable() {
        String discord = "discordExample";

        when(userRepository.findByDiscordName(discord)).thenReturn(Optional.empty());

        userService.isDiscordAvailable(discord);
    }

    @Test
    public void testIsDiscordNameAvailable_WhenDiscordNameIsNotAvailable() {
        String discord = "discordExample";

        when(userRepository.findByDiscordName(discord)).thenReturn(Optional.of(UserCreator.createValidUserModel()));

        UserException exception = assertThrows(UserException.class, () -> userService.isDiscordAvailable(discord));
        assertEquals("Discord already registered", exception.getMessage());
    }
}