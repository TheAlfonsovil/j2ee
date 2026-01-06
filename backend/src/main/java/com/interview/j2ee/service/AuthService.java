package com.interview.j2ee.service;

import com.interview.j2ee.dto.JwtResponse;
import com.interview.j2ee.dto.LoginRequest;
import com.interview.j2ee.entity.RefreshToken;
import com.interview.j2ee.entity.User;
import com.interview.j2ee.exception.BadRequestException;
import com.interview.j2ee.repository.RefreshTokenRepository;
import com.interview.j2ee.repository.UserRepository;
import com.interview.j2ee.security.JwtTokenProvider;
import com.interview.j2ee.security.UserPrincipal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class AuthService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AuthService.class);
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, RefreshTokenRepository refreshTokenRepository, UserRepository userRepository, AuditLogService auditLogService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.auditLogService = auditLogService;
    }

    @Value("${jwt.refresh-expiration}")
    private long refreshTokenExpiration;

    @Transactional
    public JwtResponse login(LoginRequest loginRequest) {
        log.debug("[DEBUG LOGIN] Attempting login for user: {}", loginRequest.getUsername());
        log.debug("[DEBUG LOGIN] Password received (plain): {}", loginRequest.getPassword());
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        log.debug("[DEBUG LOGIN] Authentication successful for user: {}", loginRequest.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String jwt = tokenProvider.generateToken(authentication);
        String refreshToken = createRefreshToken(userPrincipal.getId());

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Log audit
        auditLogService.logAction(
                userPrincipal.getUsername(),
                "LOGIN",
                "User",
                userPrincipal.getId(),
                "User logged in successfully",
                null
        );

        log.info("User {} logged in successfully", loginRequest.getUsername());

        return JwtResponse.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken)
                .id(userPrincipal.getId())
                .username(userPrincipal.getUsername())
                .email(userPrincipal.getEmail())
                .fullName(userPrincipal.getFullName())
                .roles(roles)
                .build();
    }

    @Transactional
    public JwtResponse refreshToken(String refreshTokenStr) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenStr)
                .orElseThrow(() -> new BadRequestException("Invalid refresh token"));

        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new BadRequestException("Refresh token expired");
        }

        User user = refreshToken.getUser();
        String newAccessToken = tokenProvider.generateTokenFromUsername(
                user.getUsername(),
                user.getId(),
                user.getRole().name()
        );

        List<String> roles = List.of("ROLE_" + user.getRole().name());

        return JwtResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshTokenStr)
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .roles(roles)
                .build();
    }

    @Transactional
    public void logout(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("User not found"));

        refreshTokenRepository.deleteByUser(user);
        
        auditLogService.logAction(
                username,
                "LOGOUT",
                "User",
                user.getId(),
                "User logged out",
                null
        );

        log.info("User {} logged out successfully", username);
    }

    private String createRefreshToken(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        // Delete old refresh tokens for this user
        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000))
                .build();

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }

    @Transactional
    public void cleanupExpiredTokens() {
        refreshTokenRepository.deleteExpiredTokens(LocalDateTime.now());
        log.info("Expired refresh tokens cleaned up");
    }
}
