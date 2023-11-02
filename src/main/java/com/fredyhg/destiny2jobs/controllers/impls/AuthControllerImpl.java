package com.fredyhg.destiny2jobs.controllers.impls;

import com.fredyhg.destiny2jobs.controllers.AuthController;
import com.fredyhg.destiny2jobs.models.AuthenticationResponse;
import com.fredyhg.destiny2jobs.models.dtos.auth.AuthenticationDto;
import com.fredyhg.destiny2jobs.services.AuthService;
import com.fredyhg.destiny2jobs.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    @PostMapping("/authenticate")
    @Override
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationDto authenticationDto) {
       return ResponseEntity.status(HttpStatus.OK).body(authService.authenticate(authenticationDto));
    }

    @PostMapping("/refresh-token")
    @Override
    public void refresToken(HttpServletRequest request, HttpServletResponse response){
        authService.refreshToken(request, response);
    }
}
