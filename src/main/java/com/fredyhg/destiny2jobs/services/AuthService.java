package com.fredyhg.destiny2jobs.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fredyhg.destiny2jobs.exceptions.UserException;
import com.fredyhg.destiny2jobs.models.AuthenticationResponse;
import com.fredyhg.destiny2jobs.models.UserModel;
import com.fredyhg.destiny2jobs.models.dtos.auth.AuthenticationDto;
import com.fredyhg.destiny2jobs.repositories.UserRepository;
import com.fredyhg.destiny2jobs.security.token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final UserService userService;

    private final TokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticationDto request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var usuario = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(usuario);
        var refreshToken = jwtService.generatedRefreshToken(usuario);
        revokeAllUserTokens(usuario);
        this.userService.saveUserToken(usuario, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void revokeAllUserTokens(UserModel user){
        var validAccountTokens = tokenRepository.findAllValidTokenByUser(user.getId());

        if(validAccountTokens.isEmpty()) return;

        validAccountTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(validAccountTokens);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response){
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String accountEmail;

        if(authHeader == null || !authHeader.startsWith("Bearer ")) return;

        refreshToken = authHeader.substring(7);
        accountEmail = jwtService.extractUsername(refreshToken);

        if(accountEmail == null) throw new UserException("Error to parse email from token");


        var account = this.userService.findByEmail(accountEmail);

        if(jwtService.isTokenValid(refreshToken, account)){
            var accessToken = jwtService.generateToken(account);
            revokeAllUserTokens(account);
            this.userService.saveUserToken(account, accessToken);

            var authResponse = AuthenticationResponse
                    .builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

            try {
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            } catch (IOException ex) {
                throw new InternalAuthenticationServiceException("Error");
            }
        }

    }


}
