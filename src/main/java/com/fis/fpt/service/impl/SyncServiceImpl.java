package com.fis.fpt.service.impl;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fis.fpt.domain.*;
import com.fis.fpt.repository.*;
import com.fis.fpt.service.S3Service;
import com.fis.fpt.service.dto.SandboxFilter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class SyncServiceImpl {

    private final AclRepository aclRepository;
    private final ApiInfoRepository apiInfoRepository;
    private final FormRepository formRepository;
    private final ProceduresRepository proceduresRepository;
    private final StorageFileRepository storageFileRepository;
    private final VersionRepository versionRepository;
    private final S3Service service;

    @Async
    public void asyncExport(SandboxFilter sandboxFilter, Path tempFile) {
        int maxAttempts = 3;
        int attempt = 0;
        boolean success = false;
        Random random = new Random();
        while (attempt < maxAttempts && !success) {
            try {
                Map<String, Object> counts = new HashMap<>();

                String checksum = generateFileWithInput(counts, tempFile, sandboxFilter);

                try (InputStream in = Files.newInputStream(tempFile)) {
                    service.publicUploadV3("sandbox/" + tempFile.getFileName().toString(), in);
                }

                success = true;
            } catch (Exception e) {
                attempt++;
                if (attempt < maxAttempts) {
                    System.err.println("Export failed. Retrying... attempt " + attempt + "/" + maxAttempts);
                    try {
                        long waitTimeMs = (long) ((Math.pow(2, attempt) + random.nextDouble()) * 500L);
                        Thread.sleep(waitTimeMs);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    @Async
    public CompletableFuture<Map<String, Object>> runImport(String fileUrl) {
        Map<String, Object> result = new HashMap<>();

        try {
            Path filePath = Files.createTempFile("workflow_import_", ".json");
            try (InputStream in = new URL(fileUrl).openStream()) {
                Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            JsonFactory factory = new JsonFactory();
            ObjectMapper mapper = new ObjectMapper(factory);
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


            try (InputStream in = Files.newInputStream(filePath);
                 JsonParser parser = factory.createParser(in)) {

                while (!parser.isClosed()) {
                    JsonToken token = parser.nextToken();
                    if (token == JsonToken.FIELD_NAME) {
                        String fieldName = parser.getCurrentName();
                        parser.nextToken();

                        switch (fieldName) {
                            case "acl":
                                long aclCount = importArray(parser, mapper, Acl.class, aclRepository);
                                result.put("acl", aclCount);
                                break;
                            case "api_info":
                                long apiCount = importArray(parser, mapper, ApiInfo.class, apiInfoRepository);
                                result.put("api_info", apiCount);
                                break;
                            case "form":
                                long formCount = importArray(parser, mapper, Form.class, formRepository);
                                result.put("form", formCount);
                                break;
                            case "procedures":
                                long procCount = importArray(parser, mapper, Procedures.class, proceduresRepository);
                                result.put("procedures", procCount);
                                break;
                            case "storage_file":
                                long sfCount = importArray(parser, mapper, StorageFile.class, storageFileRepository);
                                result.put("storage_file", sfCount);
                                break;
                            case "version":
                                long verCount = importArray(parser, mapper, Version.class, versionRepository);
                                result.put("version", verCount);
                                break;
                            default:
                                parser.skipChildren();
                        }
                    }
                }
            }
            result.put("status", "Import success");
            Files.deleteIfExists(filePath);
        } catch (Exception e) {
            result.put("status", "PROCESSING");
        }
        return CompletableFuture.completedFuture(result);
    }

    private <T> long importArray(JsonParser parser, ObjectMapper mapper, Class<T> clazz, JpaRepository<T, ?> repository) throws IOException {
        if (parser.currentToken() != JsonToken.START_ARRAY) {
            parser.skipChildren();
            return 0;
        }

        List<T> batch = new ArrayList<>();
        int batchSize = 500;
        long totalCount = 0;

        while (parser.nextToken() != JsonToken.END_ARRAY) {
            T entity = mapper.readValue(parser, clazz);
            batch.add(entity);

            if (batch.size() >= batchSize) {
                repository.saveAll(batch);
                totalCount += batch.size();
                batch.clear();
            }
        }

        if (!batch.isEmpty()) {
            repository.saveAll(batch);
            totalCount += batch.size();
        }

        return totalCount;
    }

    @NotNull
    private String generateFileWithInput(Map<String, Object> counts, Path tempFile, SandboxFilter sandboxFilter) throws IOException, NoSuchAlgorithmException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try (OutputStream out = Files.newOutputStream(tempFile);
             JsonGenerator generator = mapper.getFactory().createGenerator(out)) {

            generator.writeStartObject();
            if (sandboxFilter.getFormIds() == null || sandboxFilter.getFormIds().isEmpty()) {
                exportEntity(generator, mapper, counts, "acl", aclRepository::findAll);
//                exportEntity(generator, mapper, counts, "api_info", apiInfoRepository::findAll);
                exportEntity(generator, mapper, counts, "form", formRepository::findAll);
                exportEntity(generator, mapper, counts, "procedures", proceduresRepository::findAll);
//                exportEntity(generator, mapper, counts, "storage_file", storageFileRepository::findAll);
                exportEntity(generator, mapper, counts, "version", versionRepository::findAll);
            } else {
                exportEntity(generator, mapper, counts, "acl", () -> aclRepository.findAllByFormIdIn(sandboxFilter.getFormIds()));
//                exportEntity(generator, mapper, counts, "api_info", apiInfoRepository::findAll);
                exportEntity(generator, mapper, counts, "form", () -> formRepository.findByFormIdIn(sandboxFilter.getFormIds()));
                exportEntity(generator, mapper, counts, "procedures", () -> proceduresRepository.findAllByFormIdIn(sandboxFilter.getFormIds()));
//                exportEntity(generator, mapper, counts, "storage_file", storageFileRepository::findAll);
                exportEntity(generator, mapper, counts, "version", () -> versionRepository.findAllByFormIdIn(sandboxFilter.getFormIds()));
            }

            generator.writeEndObject();
        }

        return calculateChecksum(tempFile);
    }

    private <T> void exportEntity(JsonGenerator generator,
                                  ObjectMapper mapper,
                                  Map<String, Object> counts,
                                  String fieldName,
                                  Supplier<List<T>> supplier) throws IOException {
        List<T> data = supplier.get();
        generator.writeFieldName(fieldName);
        mapper.writeValue(generator, data);
        counts.put(fieldName, data.size());
    }

    private String calculateChecksum(Path file) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        try (InputStream fis = Files.newInputStream(file);
             DigestInputStream dis = new DigestInputStream(fis, digest)) {
            byte[] buffer = new byte[8192];
            while (dis.read(buffer) != -1) {
            }
        }
        byte[] hash = digest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
