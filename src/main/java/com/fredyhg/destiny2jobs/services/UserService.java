package com.fredyhg.destiny2jobs.services;

import com.fredyhg.destiny2jobs.exceptions.user.UserAlreadyExistsException;
import com.fredyhg.destiny2jobs.exceptions.user.UserException;
import com.fredyhg.destiny2jobs.exceptions.user.UserNotFoundException;
import com.fredyhg.destiny2jobs.models.AuthenticationResponse;
import com.fredyhg.destiny2jobs.models.UserModel;
import com.fredyhg.destiny2jobs.models.dtos.user.UserGetDto;
import com.fredyhg.destiny2jobs.models.dtos.user.UserPostDto;
import com.fredyhg.destiny2jobs.repositories.UserRepository;
import com.fredyhg.destiny2jobs.security.token.Token;
import com.fredyhg.destiny2jobs.security.token.TokenRepository;
import com.fredyhg.destiny2jobs.security.token.TokenType;
import com.fredyhg.destiny2jobs.utils.ModelMappers;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse createUser(UserPostDto user) {

        checkIfEmailIsAlreadyRegistered(user.getEmail());

        UserModel userToBeSaved = ModelMappers.userPostDtoToUserModel(user);

        userToBeSaved.setPassword(passwordEncoder.encode(user.getPassword()));

        var savedUser = userRepository.save(userToBeSaved);
        var jwtToken = jwtService.generateToken(savedUser);
        var refreshToken = jwtService.generatedRefreshToken(savedUser);

        this.saveUserToken(savedUser, jwtToken);

        return AuthenticationResponse
                .builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();

    }

    public Page<UserGetDto> listAllUsers(Pageable pageable) {
        Page<UserModel> users = userRepository.findAll(pageable);
        return users.map(ModelMappers::userModelToUserGetDto);
    }

    public UserModel findByEmail(String email){
        return ensureEmailExists(email);
    }

    public void deleteById(UUID uuid) {
        ensureIdExists(uuid);
        userRepository.deleteById(uuid);
    }

    public void saveUserToken(UserModel user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }


    public UserGetDto getCurrentUserData(HttpServletRequest request, HttpServletResponse response) {
        return ModelMappers.userModelToUserGetDto(userExistsByToken(request));
    }

    public UserModel userExistsByToken(HttpServletRequest request){
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UserException("Invalid Token");


        String accountEmail = jwtService.extractUsername(authHeader.substring(7));

        Optional<UserModel> accountExists = userRepository.findByEmail(accountEmail);

        if(accountExists.isEmpty())
            throw new UserNotFoundException("User not exists");

        return accountExists.get();
    }

    public void isEmailAvailable(String email) {

        Optional<UserModel> userByEmail = userRepository.findByEmail(email);

        if(userByEmail.isPresent())
            throw new UserAlreadyExistsException("Email already registered");

    }

    public void isDiscordAvailable(String discord) {
        Optional<UserModel> userByDiscord = userRepository.findByDiscordName(discord);

        if(userByDiscord.isPresent())
            throw new UserAlreadyExistsException("Discord already registered");

    }

    private void checkIfEmailIsAlreadyRegistered(final String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            throw new UserAlreadyExistsException("Email already registered");
        });
    }

    private void checkIfIdIsAlreadyRegistered(final UUID id) {
        userRepository.findById(id).ifPresent(user -> {
            throw new UserAlreadyExistsException("Email already registered");
        });
    }

    private UserModel ensureEmailExists(final String email) {
        Optional<UserModel> userExist = userRepository.findByEmail(email);

        if(userExist.isEmpty())
            throw new UserNotFoundException("Email not found");

        return userExist.get();
    }

    private void ensureIdExists(final UUID id) {
        if(userRepository.findById(id).isEmpty())
            throw new UserNotFoundException("User not found");

    }
}
