package com.fis.fpt.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class SandboxFilter {
    private List<String> formIds;
    private List<String> versionIds;
    private String checkSum;
}
