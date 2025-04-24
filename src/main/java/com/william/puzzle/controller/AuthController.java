package com.william.puzzle.controller;

import com.william.puzzle.constants.ApiPaths;
import com.william.puzzle.dto.LoginRequest;
import com.william.puzzle.dto.LoginResponse;
import com.william.puzzle.dto.LogoutRequest;
import com.william.puzzle.dto.RefreshJwtRequest;
import com.william.puzzle.dto.RefreshJwtResponse;
import com.william.puzzle.service.AuthServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPaths.AUTH)
public class AuthController {
    private final AuthServiceImpl authService;

    @PostMapping(ApiPaths.LOGIN)
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request){
        var loginResponse = this.authService.login(request);
        return ResponseEntity.status(200).body(loginResponse);
    }

    @PostMapping(ApiPaths.LOGOUT)
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest request){
        authService.logout(request.accessToken(), request.refreshToken());
        return ResponseEntity.status(204).build();
    }

    @PostMapping(ApiPaths.REFRESH)
    public ResponseEntity<RefreshJwtResponse> refresh(@Valid @RequestBody RefreshJwtRequest request){
        var refreshResponse = authService.refreshToken(request.refreshToken());
        return ResponseEntity.status(200).body(refreshResponse);
    }
}
