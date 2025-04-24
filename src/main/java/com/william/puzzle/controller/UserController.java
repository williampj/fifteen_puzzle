package com.william.puzzle.controller;

import com.william.puzzle.constants.ApiPaths;
import com.william.puzzle.dto.SignupRequest;
import com.william.puzzle.dto.SignupResponse;
import com.william.puzzle.enums.Role;
import com.william.puzzle.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPaths.USERS)
public class UserController {
    private final UserServiceImpl userService;

    @PostMapping()
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest request) {
        var signupResponse = this.userService.signup(request, Role.PLAYER);
        return ResponseEntity.status(201).body(signupResponse);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId, false);
        return ResponseEntity.status(204).build();
    }
}
