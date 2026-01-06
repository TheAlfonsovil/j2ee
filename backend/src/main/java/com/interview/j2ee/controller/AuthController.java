package com.interview.j2ee.controller;

import com.interview.j2ee.dto.ApiResponse;
import com.interview.j2ee.dto.JwtResponse;
import com.interview.j2ee.dto.LoginRequest;
import com.interview.j2ee.dto.RefreshTokenRequest;
import com.interview.j2ee.security.UserPrincipal;
import com.interview.j2ee.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
// import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
// import org.springframework.beans.factory.annotation.Autowired;

// @RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication and authorization endpoints")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT tokens")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token", description = "Generate new access token using refresh token")
    public ResponseEntity<JwtResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        JwtResponse response = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "User logout", description = "Logout user and invalidate refresh tokens")
    public ResponseEntity<ApiResponse> logout(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        // Only logout if user is authenticated
        if (userPrincipal != null) {
            authService.logout(userPrincipal.getUsername());
        }
        return ResponseEntity.ok(new ApiResponse(true, "Logged out successfully"));
    }
}
