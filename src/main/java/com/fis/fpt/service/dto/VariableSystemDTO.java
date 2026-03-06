package com.fis.fpt.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VariableSystemDTO {

    private String id;

    private String variableSystem;

    private String createdBy;

    private Long userId;

    private String orgIn;

    private Long custId;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;
}
