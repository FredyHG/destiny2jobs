package com.fredyhg.destiny2jobs.controllers.impls;

import com.fredyhg.destiny2jobs.controllers.UserController;
import com.fredyhg.destiny2jobs.models.AuthenticationResponse;
import com.fredyhg.destiny2jobs.models.dtos.user.UserGetDto;
import com.fredyhg.destiny2jobs.models.dtos.user.UserPostDto;
import com.fredyhg.destiny2jobs.services.UserService;
import com.fredyhg.destiny2jobs.utils.models.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @PostMapping
    @Override
    public ResponseEntity<AuthenticationResponse> createUser(@RequestBody UserPostDto user){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @GetMapping
    @Override
    public ResponseEntity<Page<UserGetDto>> listAllUsers(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.listAllUsers(pageable));
    }

    @DeleteMapping
    @Override
    public ResponseEntity<ResponseMessage> deleteUser(@RequestParam(name = "id") UUID uuid) {
        userService.deleteById(uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseMessage("User deleted successfully"));
    }

    @GetMapping("/data")
    @Override
    public ResponseEntity<UserGetDto> getCurrentUserData(HttpServletRequest request, HttpServletResponse response){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getCurrentUserData(request, response));
    }

    @GetMapping("/isEmailAvailable")
    @Override
    public ResponseEntity<ResponseMessage> isEmailAvailable(@RequestParam String email) {
        userService.isEmailAvailable(email);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Email available"));
    }

    @GetMapping("/isDiscordAvailable")
    @Override
    public ResponseEntity<ResponseMessage> isDiscordAvailable(String discord) {
        userService.isDiscordAvailable(discord);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Email available"));
    }
}
