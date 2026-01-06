package com.interview.j2ee.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.interview.j2ee.dto.AuditLogDTO;
import com.interview.j2ee.entity.AuditLog;

@Mapper(componentModel = "spring")
public interface AuditLogMapper {

    AuditLogDTO toDTO(AuditLog auditLog);

    List<AuditLogDTO> toDTOList(List<AuditLog> auditLogs);
}
