package com.fis.fpt.service.dto;

import lombok.Data;

@Data
public class VariableSystemAclRequest {
    private String assignee;
    private Long assigneeId;
    private Long permType;
    private String perm;
}
