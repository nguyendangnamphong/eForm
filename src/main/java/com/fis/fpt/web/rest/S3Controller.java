package com.fis.fpt.web.rest;

import com.fis.fpt.security.SecurityUtils;
import com.fis.fpt.security.UserInFoDetails;
import com.fis.fpt.service.dto.PresignedUrlResponse;
import com.fis.fpt.service.dto.ResponseS3Upload;
import com.fis.fpt.service.dto.StorageFileDTO;
import com.fis.fpt.service.impl.S3ServiceImpl;
import com.fis.fpt.utils.TimeUtil;
import com.fis.fpt.web.rest.errors.BadRequestAlertException;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Paths;
import java.time.Instant;


@Log4j2
@RestController
@RequestMapping("/api/s3")
@RequiredArgsConstructor
public class S3Controller {
    private final S3ServiceImpl s3Service;
    private static final String ENTITY_NAME = "eformStorageFile";

    @PostMapping("/upload")
    public Mono<ResponseEntity<String>> uploadFile(@RequestBody MultipartFile file) {
        return Mono.fromCallable(() -> {
            InputStream inputStreamInput = file.getInputStream();
            String keyName = getKeyName(file);
            return ResponseEntity.ok(s3Service.publicUpload(keyName, inputStreamInput));
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @PostMapping("/uploadViewFile")
    public Mono<ResponseEntity<ResponseS3Upload>> uploadViewFile(@RequestBody MultipartFile file) {
        return Mono.fromCallable(() -> {
            InputStream inputStreamInput = file.getInputStream();
//            String extension = Objects.requireNonNull(FilenameUtils.getExtension(file.getOriginalFilename())).toLowerCase();
//            boolean isPdf = "pdf".equals(extension);
            String keyName = getKeyName(file);
            return ResponseEntity.ok(s3Service.publicUpload2(keyName, inputStreamInput));
            // if (isPdf) {
            //    ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //    compressPdf(inputStreamInput, baos);
            //     InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
            //     return ResponseEntity.ok(s3Service.publicUpload2(keyName, inputStream));
            // } else {
            //     return (ResponseEntity<?>) ResponseEntity.ok(s3Service.publicUpload2(keyName, inputStreamInput));
            // }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public void compressPdf(InputStream inputPdf, OutputStream outputPdf) throws IOException {
        try (
            PdfReader reader = new PdfReader(inputPdf);
            PdfWriter writer = new PdfWriter(outputPdf, new WriterProperties().setFullCompressionMode(true));
            PdfDocument pdfDoc = new PdfDocument(reader, writer)
        ) {
            int numberOfPages = pdfDoc.getNumberOfPages();

            for (int i = 1; i <= numberOfPages; i++) {
                PdfPage page = pdfDoc.getPage(i);
                PdfResources resources = page.getResources();
                PdfDictionary xObjectDict = resources.getPdfObject().getAsDictionary(PdfName.XObject);

                if (xObjectDict == null) continue;

                for (PdfName name : xObjectDict.keySet()) {
                    PdfObject obj = xObjectDict.get(name);
                    if (obj instanceof PdfIndirectReference) {
                        obj = ((PdfIndirectReference) obj).getRefersTo();
                    }

                    if (!(obj instanceof PdfStream)) continue;

                    PdfStream stream = (PdfStream) obj;
                    PdfName subtype = stream.getAsName(PdfName.Subtype);

                    if (PdfName.Image.equals(subtype)) {
                        PdfImageXObject imageObject = new PdfImageXObject(stream);
                        BufferedImage bufferedImage = imageObject.getBufferedImage();

                        if (bufferedImage == null) continue;

                        ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
                        ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
                        ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
                        jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                        jpgWriteParam.setCompressionQuality(0.3f);

                        try (ImageOutputStream ios = ImageIO.createImageOutputStream(imgBytes)) {
                            jpgWriter.setOutput(ios);
                            jpgWriter.write(null, new javax.imageio.IIOImage(bufferedImage, null, null), jpgWriteParam);
                            jpgWriter.dispose();
                        }

                        ImageData compressedImageData = ImageDataFactory.create(imgBytes.toByteArray());
                        PdfImageXObject compressedImage = new PdfImageXObject(compressedImageData);

                        xObjectDict.put(name, compressedImage.getPdfObject());
                    }
                }
            }
        }
    }

    @PostMapping("/expire/upload")
    public Mono<ResponseEntity<String>> uploadFileExpire(@RequestBody MultipartFile file) {
        return Mono.fromCallable(() -> {
            InputStream inputStream = file.getInputStream();
            String keyName = getKeyName(file);
            return ResponseEntity.ok(s3Service.uploadFileToS3Cloud(keyName, inputStream));
        }).subscribeOn(Schedulers.boundedElastic())
            .onErrorResume(ex -> {
                log.error("Upload failed", ex);
                String errorMessage = "Upload failed: " + ex.getMessage();
                return Mono.just(ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorMessage));
            });
    }

    @NotNull
    private String getKeyName(@RequestBody MultipartFile file) {
        UserInFoDetails userEntity = SecurityUtils.getInfoCurrentUserLogin();
        String fileName = Instant.now().toEpochMilli() + "_" + file.getOriginalFilename();
        String keyName = "";
        if (userEntity == null) {
            keyName += "anonymous";
        } else {
            keyName += userEntity.getId();
        }
        keyName += "/" + TimeUtil.fomatTime(TimeUtil.yyyyMMdd, Instant.now()) + "/" + fileName;
        return keyName;
    }

    private String getFileName(String name) {
        UserInFoDetails userEntity = SecurityUtils.getInfoCurrentUserLogin();
        String fileName = Instant.now().toEpochMilli() + "_" + name;
        String keyName = "";
        if (userEntity == null) {
            keyName += "anonymous";
        } else {
            keyName += userEntity.getId();
        }
        keyName += "/" + TimeUtil.fomatTime(TimeUtil.yyyyMMdd, Instant.now()) + "/" + fileName;
        return keyName;
    }


    @PostMapping("/downloadFile")
    public ResponseEntity<byte[]> downloadStorageFile(@RequestBody PresignedUrlResponse urlResponse) throws IOException {
        InputStream inputStream = null;
        byte[] fileBytes;

        try {
            inputStream = s3Service.downloadFileIs(
                urlResponse.getBucket(),
                urlResponse.getKeyPath(),
                urlResponse.getBeanName()
            );

            fileBytes = inputStream.readAllBytes();

        } catch (Exception e) {
            String message = String.format(
                "Failed to read file from S3 [bucket=%s, path=%s, beanName=%s]",
                urlResponse.getBucket(),
                urlResponse.getKeyPath(),
                urlResponse.getBeanName()
            );
            throw new RuntimeException(message, e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {}
            }
        }

        String fileName = String.valueOf(Paths.get(urlResponse.getKeyPath()).getFileName());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");

        return ResponseEntity.ok()
            .headers(headers)
            .body(fileBytes);
    }


    @PostMapping("/upload/d2s")
    public ResponseEntity<PresignedUrlResponse> getPresignedUrl(@RequestBody StorageFileDTO storageFile) {
        log.debug("REST request to generate presigned URL for StorageFile : {}", storageFile);

        if (storageFile.getFileSize() == null || storageFile.getFileSize() <= 0) {
            throw new BadRequestAlertException("Valid fileSize must be provided", ENTITY_NAME, "invalidfilesize");
        }
        String beanName = "s3ClientSecond";
        try {
            PresignedUrlResponse presignedUrl = s3Service.generatePresignedUrl(
                this.getFileName(storageFile.getFileName()),
                storageFile.getFileSize(),
                beanName,
                storageFile.getContentType()
            );

            return ResponseEntity.ok()
                .body(presignedUrl);
        } catch (Exception e) {
            String message = String.format(
                "Failed to generate presigned URL [fileName=%s, contentType=%s, fileSize=%s]",
                storageFile.getFileName(),
                storageFile.getContentType(),
                storageFile.getFileSize()
            );
            throw new RuntimeException(message, e);
        }
    }

    @PostMapping("/upload/public/d2s")
    public ResponseEntity<PresignedUrlResponse> getPublicPresignedUrl(@RequestBody StorageFileDTO storageFile) {
        log.debug("REST request to generate presigned URL for StorageFile : {}", storageFile);

        if (storageFile.getFileSize() == null || storageFile.getFileSize() <= 0) {
            throw new BadRequestAlertException("Valid fileSize must be provided", ENTITY_NAME, "invalidfilesize");
        }
        String beanName = "s3ClientSecond";
        try {
            PresignedUrlResponse presignedUrl = s3Service.generatePublicPresignedUrl(
                this.getFileName(storageFile.getFileName()),
                storageFile.getFileSize(),
                beanName,
                storageFile.getContentType()
            );

            return ResponseEntity.ok()
                .body(presignedUrl);
        } catch (Exception e) {
            String message = String.format(
                "Failed to generate presigned URL [fileName=%s, contentType=%s, fileSize=%s]",
                storageFile.getFileName(),
                storageFile.getContentType(),
                storageFile.getFileSize()
            );
            throw new RuntimeException(message, e);
        }
    }
}
