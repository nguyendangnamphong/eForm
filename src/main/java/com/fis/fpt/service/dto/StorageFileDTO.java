package com.fis.fpt.service.dto;

import lombok.Data;

@Data
public class StorageFileDTO {
    private String fileName;
    private Long fileSize;
    private String contentType;
}
