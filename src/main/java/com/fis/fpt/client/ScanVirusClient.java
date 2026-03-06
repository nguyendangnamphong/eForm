package com.fis.fpt.client;

import feign.Headers;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "scanVirusClient", url = "${application.uaa-name}")
public interface ScanVirusClient {
    @PostMapping(value = "/api/scan_virus/scan", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Headers("Content-Type: multipart/form-data")
    String scan(@RequestPart("file") MultipartFile file);
}
