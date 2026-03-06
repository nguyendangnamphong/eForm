package com.fis.fpt.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VariableSystemAclDTO {
    private String id;

    private String varId;

    private String perm;

    private Long permType;

    private String assignee;

    private Long assigneeId;

    private Long authorizerId;

    private String authorizer;

    private Long userId;

    private Long custId;

    private String orgIn;
}
