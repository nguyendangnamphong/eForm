package com.fis.fpt.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fis.fpt.domain.VariableSystem;
import com.fis.fpt.security.SecurityUtils;
import com.fis.fpt.service.dto.VariableSystemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Mapper for converting between VariableSystem entity and DTOs.
 * Only the 'variableSystem' field is updated in update methods.
 */
@Component
@RequiredArgsConstructor
public class VariableSystemMapper {

    private final ObjectMapper objectMapper;

    /**
     * Creates a new VariableSystem entity from DTO.
     * Used for initial creation.
     */
    public VariableSystem toEntity(VariableSystemDTO dto, Long userId, String orgIn, Long custId, Instant currentTime) {
        String login = SecurityUtils.getCurrentUserLogin().orElse("system"); // fallback

        VariableSystem entity = new VariableSystem();
        entity.setId(dto.getId());
        entity.setVariableSystem(dto.getVariableSystem());
        entity.setCreatedBy(login.trim());
        entity.setUserId(userId);
        entity.setOrgIn(orgIn);
        entity.setCustId(custId);
        entity.setCreatedDate(currentTime);
        entity.setLastModifiedDate(currentTime);
        entity.setLastModifiedBy(login.trim());
        return entity;
    }


    /**
     * Updates only the 'variableSystem' field and audit fields.
     * Ignores all other fields in DTO.
     */
    public void updateEntity(VariableSystemDTO dto, VariableSystem entity, Instant currentTime) throws JsonProcessingException {
        String login = SecurityUtils.getCurrentUserLogin().orElse("system");

        if (dto.getVariableSystem() != null && !dto.getVariableSystem().isEmpty()) {
            entity.setVariableSystem(dto.getVariableSystem());
        }

        entity.setLastModifiedDate(currentTime);
        entity.setLastModifiedBy(login.trim());

        // Future: if you add JSON fields like variableArr, use objectMapper
        // e.g., entity.setSomeJson(objectMapper.writeValueAsString(dto.getSomeList()));
    }

    /**
     * Optional: Duplicate method (mirroring your FormMapper style)
     */
    public VariableSystem toDuplicateEntity(VariableSystem source, VariableSystemDTO dto, Long userId, String orgIn, Long custId, Instant currentTime) {
        String login = SecurityUtils.getCurrentUserLogin().orElse("system");

        VariableSystem entity = new VariableSystem();
        entity.setId(dto.getId());
        entity.setVariableSystem(dto.getVariableSystem() != null ? dto.getVariableSystem() : source.getVariableSystem());
        entity.setCreatedBy(login.trim());
        entity.setUserId(userId);
        entity.setOrgIn(orgIn);
        entity.setCustId(custId);
        entity.setCreatedDate(currentTime);
        entity.setLastModifiedDate(currentTime);
        entity.setLastModifiedBy(login.trim());
        return entity;
    }
}
