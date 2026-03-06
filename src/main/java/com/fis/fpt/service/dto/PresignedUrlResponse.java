package com.fis.fpt.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PresignedUrlResponse {
    String bucket;
    String keyPath;
    String presignedUrl;
    String beanName;
    String publicUrl;
}
