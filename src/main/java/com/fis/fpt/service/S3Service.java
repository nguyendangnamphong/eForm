package com.fis.fpt.service;

import com.fis.fpt.service.dto.PresignedUrlResponse;
import com.fis.fpt.service.dto.ResponseS3Upload;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface S3Service {

    String uploadFileToS3Cloud(String keyName, InputStream inputStream);
    void publicUploadV3(String keyName, InputStream file) throws IOException;
    String generateUrl(String path);
    String publicUpload(String keyName, InputStream file) throws IOException;
    ResponseS3Upload publicUpload2(String keyName, InputStream file) throws IOException;
    InputStream downloadFileIs(String bucket, String keyName, String beanName) throws IOException;
    PresignedUrlResponse generatePresignedUrl(String keyName, long fileSize, String beanName, String contentType);
    PresignedUrlResponse generatePublicPresignedUrl(String keyName, long fileSize, String beanName, String contentType);
}
