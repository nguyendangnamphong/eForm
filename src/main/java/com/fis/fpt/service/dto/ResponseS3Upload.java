package com.fis.fpt.service.dto;

import lombok.Data;

@Data
public class ResponseS3Upload {
    String bucketName;
    String keyName;
    String beanName = "s3ClientSecond";

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public ResponseS3Upload(String bucketName, String keyName) {
        this.bucketName = bucketName;
        this.keyName = keyName;
    }
}
