package com.fis.fpt.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.services.s3.S3Client;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class S3ClientWrapper {
    private S3Client s3Client;
    private StorageServiceConfigDTO storageConfig;
}
