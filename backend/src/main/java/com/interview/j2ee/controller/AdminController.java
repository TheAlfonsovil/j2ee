package com.interview.j2ee.controller;

import com.interview.j2ee.dto.AuditLogDTO;
import com.interview.j2ee.dto.UserDTO;
import com.interview.j2ee.mapper.AuditLogMapper;
import com.interview.j2ee.service.AuditLogService;
import com.interview.j2ee.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
// Lombok removed: explicit constructor used
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")


@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin", description = "Admin-only endpoints")
public class AdminController {
    private final UserService userService;
    private final AuditLogService auditLogService;
    private final AuditLogMapper auditLogMapper;

    public AdminController(UserService userService, AuditLogService auditLogService, AuditLogMapper auditLogMapper) {
        this.userService = userService;
        this.auditLogService = auditLogService;
        this.auditLogMapper = auditLogMapper;
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserDTO> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/audit-logs")
    @Operation(summary = "Get audit logs", description = "Get paginated audit logs (Admin only)")
    public ResponseEntity<Page<AuditLogDTO>> getAuditLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<AuditLogDTO> logs = auditLogService.getAllAuditLogs(pageable).map(auditLogMapper::toDTO);
        return ResponseEntity.ok(logs);
    }
}
