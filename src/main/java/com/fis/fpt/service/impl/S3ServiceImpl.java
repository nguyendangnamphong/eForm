package com.fis.fpt.service.impl;

import com.fis.fpt.config.ApplicationProperties;
import com.fis.fpt.domain.StorageFile;
import com.fis.fpt.repository.StorageFileRepository;
import com.fis.fpt.security.SecurityUtils;
import com.fis.fpt.security.UserInFoDetails;
import com.fis.fpt.service.S3Service;
import com.fis.fpt.service.dto.PresignedUrlResponse;
import com.fis.fpt.service.dto.ResponseS3Upload;
import com.fis.fpt.service.dto.S3ClientWrapper;
import com.fis.fpt.service.dto.S3ClientWrapperV2;
import com.fis.fpt.utils.IDGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Log4j2
public class S3ServiceImpl implements S3Service {
    private final ApplicationContext context;
    private final StorageFileRepository storageFileRepository;
    private final ApplicationProperties applicationProperties;
    @Value("econ-eform-dev-2-hn2")
    private String bucketName;

    @Value("${fptcloud.s3.expTime}")
    private long expTime;

    private static final Set<String> VALID_CONTENT_TYPES = new HashSet<>(Set.of(
        "application/octet-stream",
        "application/pdf",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        "application/vnd.openxmlformats-officedocument.presentationml.presentation",
        "image/jpeg",
        "image/png"
    ));

    private static final Pattern MIME_TYPE_PATTERN = Pattern.compile("^[a-zA-Z0-9!#$&^\\-+.]+/[a-zA-Z0-9!#$&^\\-+.]+$");

    @Override
    public String uploadFileToS3Cloud(String keyName, InputStream inputStream) {
        try {
            S3ClientWrapperV2 s3Client = (S3ClientWrapperV2) context.getBean(applicationProperties.getS3BeanSecond());
            s3Client.getS3Client().putObject(PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build(),
                RequestBody.fromInputStream(inputStream, inputStream.available()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        StorageFile storageFile = new StorageFile();
        UserInFoDetails currentUser = SecurityUtils.getInfoCurrentUserLogin();
        Instant currentTime = Instant.now();
        if (currentUser == null) {
            storageFile.setId(IDGenerator.generateIDSuffix(0L));
            storageFile.setUserId(-1L);
            storageFile.setCreatedBy("anonymous");
            storageFile.setOrgIn("");
            storageFile.setCustId(-1L);
            storageFile.setLastModifiedBy("anonymous");
        } else {
            storageFile.setId(IDGenerator.generateIDSuffix(currentUser.getId()));
            storageFile.setUserId(currentUser.getId());
            storageFile.setCreatedBy(currentUser.getLogin().trim());
            storageFile.setOrgIn(currentUser.getOrgIn());
            storageFile.setCustId(currentUser.getCustId());
            storageFile.setLastModifiedBy(currentUser.getLogin().trim());
        }

        storageFile.setBucket(bucketName);
        storageFile.setPath(keyName);
        storageFile.setStatus(1);
        storageFile.setCreatedDate(currentTime);
        storageFile.setLastModifiedDate(currentTime);
        storageFile.setBeanName("s3ClientSecond");
        storageFileRepository.save(storageFile);
        String url = "https://" + bucketName + ".s3-han02.fptcloud.com/" + keyName;
        return url;
    }

    @Override
    public void publicUploadV3(String keyName, InputStream inputStream) {
        try {
            S3ClientWrapperV2 s3Client = (S3ClientWrapperV2) context.getBean(applicationProperties.getS3BeanSecond());
            s3Client.getS3Client().putObject(PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build(),
                RequestBody.fromInputStream(inputStream, inputStream.available()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public String generateUrl(String path) {
        S3ClientWrapperV2 s3Client = (S3ClientWrapperV2) context.getBean(applicationProperties.getS3BeanSecond());
        URL presignedUrl = s3Client.getS3Client().utilities()
            .getUrl(builder -> builder.bucket(bucketName)
                .key(path)
                .endpoint(URI.create(s3Client.getEndpointOverride())));
        if (presignedUrl == null || StringUtils.isEmpty(presignedUrl.toString())) {
            log.warn("[generatePresignedUrl] - Generate error");
            return null;
        }
        return presignedUrl.toString();
    }

    @Override
    public String publicUpload(String keyName, InputStream inputStream) throws IOException {
        try {
            S3ClientWrapperV2 s3Client = (S3ClientWrapperV2) context.getBean(applicationProperties.getS3BeanSecond());
            s3Client.getS3Client().putObject(PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build(),
                RequestBody.fromInputStream(inputStream, inputStream.available()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        StorageFile storageFile = new StorageFile();
        UserInFoDetails currentUser = SecurityUtils.getInfoCurrentUserLogin();
        Instant currentTime = Instant.now();
        if (currentUser == null) {
            storageFile.setId(IDGenerator.generateIDSuffix(0L));
            storageFile.setUserId(-1L);
            storageFile.setCreatedBy("anonymous");
            storageFile.setOrgIn("");
            storageFile.setCustId(-1L);
            storageFile.setLastModifiedBy("anonymous");
        } else {
            storageFile.setId(IDGenerator.generateIDSuffix(currentUser.getId()));
            storageFile.setUserId(currentUser.getId());
            storageFile.setCreatedBy(currentUser.getLogin().trim());
            storageFile.setOrgIn(currentUser.getOrgIn());
            storageFile.setCustId(currentUser.getCustId());
            storageFile.setLastModifiedBy(currentUser.getLogin().trim());
        }

        storageFile.setBucket(bucketName);
        storageFile.setPath(keyName);
        storageFile.setStatus(1);
        storageFile.setCreatedDate(currentTime);
        storageFile.setLastModifiedDate(currentTime);
        storageFile.setBeanName("s3ClientSecond");
        storageFileRepository.save(storageFile);
        String url = "https://" + bucketName + ".s3-han02.fptcloud.com/" + keyName;
        return url;
    }

    @Override
    public ResponseS3Upload publicUpload2(String keyName, InputStream inputStream) throws IOException {
        try {
            S3ClientWrapperV2 s3Client = (S3ClientWrapperV2) context.getBean(applicationProperties.getS3BeanSecond());
            s3Client.getS3Client().putObject(PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build(),
                RequestBody.fromInputStream(inputStream, inputStream.available()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        StorageFile storageFile = new StorageFile();
        UserInFoDetails currentUser = SecurityUtils.getInfoCurrentUserLogin();
        Instant currentTime = Instant.now();
        if (currentUser == null) {
            storageFile.setId(IDGenerator.generateIDSuffix(0L));
            storageFile.setUserId(-1L);
            storageFile.setCreatedBy("anonymous");
            storageFile.setOrgIn("");
            storageFile.setCustId(-1L);
            storageFile.setLastModifiedBy("anonymous");
        } else {
            storageFile.setId(IDGenerator.generateIDSuffix(currentUser.getId()));
            storageFile.setUserId(currentUser.getId());
            storageFile.setCreatedBy(currentUser.getLogin().trim());
            storageFile.setOrgIn(currentUser.getOrgIn());
            storageFile.setCustId(currentUser.getCustId());
            storageFile.setLastModifiedBy(currentUser.getLogin().trim());
        }

        storageFile.setBucket(bucketName);
        storageFile.setPath(keyName);
        storageFile.setStatus(1);
        storageFile.setCreatedDate(currentTime);
        storageFile.setLastModifiedDate(currentTime);
        storageFile.setBeanName("s3ClientSecond");
        storageFileRepository.save(storageFile);

        return new ResponseS3Upload(bucketName, keyName);
    }

    @Override
    public InputStream downloadFileIs(String bucket, String keyName, String beanName) {
        Object bean = context.getBean(
            StringUtils.isEmpty(beanName) ? applicationProperties.getS3BeanDefault() : beanName
        );

        S3Client s3Client;
        if (bean instanceof S3ClientWrapper) {
            s3Client = ((S3ClientWrapper) bean).getS3Client();
        } else if (bean instanceof S3ClientWrapperV2) {
            s3Client =  ((S3ClientWrapperV2) bean).getS3Client();
        } else {
            throw new IllegalStateException("Invalid S3 bean: " + bean.getClass().getName());
        }

        return s3Client.getObject(GetObjectRequest.builder()
            .bucket(bucket)
            .key(keyName)
            .build());
    }

    private String generateDefaultKey(String contentType) {
        String extension;
        String type = contentType != null ? contentType.toLowerCase() : "";

        switch (type) {
            case "image/jpeg":
                extension = ".jpg";
                break;
            case "image/png":
                extension = ".png";
                break;
            case "application/pdf":
                extension = ".pdf";
                break;
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                extension = ".docx";
                break;
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                extension = ".xlsx";
                break;
            case "application/vnd.openxmlformats-officedocument.presentationml.presentation":
                extension = ".pptx";
                break;
            default:
                extension = ".bin";
                break;
        }
        return "uploads/" + UUID.randomUUID() + extension;
    }

    private boolean  isValidContentType(String contentType) {
        return VALID_CONTENT_TYPES.contains(contentType.toLowerCase()) || MIME_TYPE_PATTERN.matcher(contentType).matches();
    }

    private S3Presigner getPresigner(String beanName) {
        try {
            if (StringUtils.isEmpty(beanName)) {
                return (S3Presigner) context.getBean(applicationProperties.getS3BeanDefault() + "preSigner");
            } else {
                return (S3Presigner) context.getBean(beanName + "preSigner");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("No S3Presigner bean found for name: " + beanName, e);
        }
    }

    private PutObjectPresignRequest buildPresignRequest(String key, long fileSize, String contentType, boolean isPublic) {
        long expirationMinutes = Math.max(1, fileSize / 1_000_000);
        Duration expiration = Duration.ofMinutes(expirationMinutes);

        return PutObjectPresignRequest.builder()
            .signatureDuration(expiration)
            .putObjectRequest(builder -> builder
                .bucket(bucketName)
                .key(key)
                .contentType(contentType)
                .contentLength(fileSize)
                .acl(isPublic ? ObjectCannedACL.PUBLIC_READ : null))
            .build();
    }

    @Override
    public PresignedUrlResponse generatePresignedUrl(String keyName, long fileSize, String beanName, String contentType) {
        String effectiveKey = StringUtils.hasText(keyName) ? keyName : generateDefaultKey(contentType);
        log.debug("Effective key before presigning: {}", effectiveKey);

        String effectiveContentType = getEffectiveContentType(contentType);
        log.debug("Effective content type: {}", effectiveContentType);

        S3Presigner preSigner = getPresigner(beanName);

        try {
            PutObjectPresignRequest presignRequest = buildPresignRequest(effectiveKey, fileSize, effectiveContentType, false);
            PresignedPutObjectRequest presignedRequest = preSigner.presignPutObject(presignRequest);
            String presignedUrl = presignedRequest.url().toString();
            log.debug("Generated presigned URL: {}", presignedUrl);

            if (presignedUrl == null || presignedUrl.isEmpty()) {
                log.warn("[generatePresignedUrl] - Generate error for bucket: {}, key: {}", bucketName, effectiveKey);
                return null;
            }

            return new PresignedUrlResponse(bucketName, keyName, presignedUrl, beanName, null);
        } catch (Exception e) {
            log.error("Error generating presigned URL for bucket: {}, key: {}, beanName: {}", bucketName, effectiveKey, beanName, e);
            throw new RuntimeException(
                String.format("Failed to generate presigned URL [bucket=%s, key=%s, beanName=%s]", bucketName, effectiveKey, beanName), e);
        }
    }


    @Override
    public PresignedUrlResponse generatePublicPresignedUrl(String keyName, long fileSize, String beanName, String contentType) {
        String effectiveKey = StringUtils.hasText(keyName) ? keyName : generateDefaultKey(contentType);
        log.debug("Effective key before presigning (public): {}", effectiveKey);

        String effectiveContentType = getEffectiveContentType(contentType);
        log.debug("Effective content type (public): {}", effectiveContentType);

        S3Presigner preSigner = getPresigner(beanName);

        try {
            PutObjectPresignRequest presignRequest = buildPresignRequest(effectiveKey, fileSize, effectiveContentType, true);
            PresignedPutObjectRequest presignedRequest = preSigner.presignPutObject(presignRequest);
            String presignedUrl = presignedRequest.url().toString();
            log.debug("Generated public presigned URL: {}", presignedUrl);

            if (presignedUrl == null || presignedUrl.isEmpty()) {
                log.warn("[generatePublicPresignedUrl] - Generate error for bucket: {}, key: {}", bucketName, effectiveKey);
                return null;
            }

            URL url = new URL(presignedUrl);
            String host = url.getHost();

            String extracted = host.substring(host.indexOf(".s3-"));

            String publicUrl = "https://" + bucketName + extracted + "/" + effectiveKey;

            return new PresignedUrlResponse(bucketName, keyName, presignedUrl, beanName, publicUrl);
        } catch (Exception e) {
            log.error("Error generating public presigned URL for bucket: {}, key: {}, beanName: {}", bucketName, effectiveKey, beanName, e);
            throw new RuntimeException(
                String.format("Failed to generate public presigned URL [bucket=%s, key=%s, beanName=%s]", bucketName, effectiveKey, beanName), e);
        }
    }

    private String getEffectiveContentType(String contentType) {
        if (StringUtils.hasText(contentType)) {
            if (!isValidContentType(contentType)) {
                throw new IllegalArgumentException("Invalid contentType: " + contentType + ". Must be a valid MIME type.");
            }
            return contentType;
        }
        return "application/octet-stream";
    }
}
