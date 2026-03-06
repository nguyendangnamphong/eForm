package com.fis.fpt.web.rest;

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
import com.fis.fpt.service.dto.SandboxPut;
import com.fis.fpt.service.impl.SyncServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;


@Log4j2
@RestController
@Transactional
@RequiredArgsConstructor
@RequestMapping("/api/sandbox")
public class SyncResource {
    private final AclRepository aclRepository;
    private final ApiInfoRepository apiInfoRepository;
    private final FormRepository formRepository;
    private final ProceduresRepository proceduresRepository;
    private final StorageFileRepository storageFileRepository;
    private final VersionRepository versionRepository;
    private final S3Service service;
    private final SyncServiceImpl syncService;

    @PostMapping("/export")
    public ResponseEntity<?> sandboxExport(@RequestBody SandboxFilter sandboxFilter) throws IOException, NoSuchAlgorithmException {
        log.debug("API call /sandbox/export");

        Map<String, Object> counts = new HashMap<>();
        Path tempFile = Files.createTempFile("workflow_export_", ".json");

        String checksum = generateFileWithInput(counts, tempFile, sandboxFilter);

        String link;
        try (InputStream in = Files.newInputStream(tempFile)) {
            link = service.publicUpload("sandbox/" + tempFile.getFileName().toString(), in);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("link", link);
        result.put("counts", counts);
        result.put("checkSum", checksum);
        result.put("status", "Export success");

        return ResponseEntity.ok(result);
    }

    @PostMapping("/export/job")
    public ResponseEntity<?> sandboxExportJob(@RequestBody SandboxFilter sandboxFilter) throws IOException, NoSuchAlgorithmException {
        log.debug("API call /sandbox/export/job");
        Map<String, Object> result = new HashMap<>();
        Path tempFile = Files.createTempFile("workflow_export_", ".json");

        String path = "sandbox/" + tempFile.getFileName().toString();
        syncService.asyncExport(sandboxFilter, tempFile);
        result.put("link", service.generateUrl(path));
        result.put("status", "PROCESSING");

        return ResponseEntity.ok(result);
    }

    @PutMapping("/import/job")
    public ResponseEntity<Map<String, Object>> sandboxImportJob(@RequestBody SandboxPut sandboxPut) throws IOException, NoSuchAlgorithmException {
        String fileUrl = sandboxPut.getLink();
        log.debug("API call /sandbox/import, fileUrl={}", fileUrl);

        syncService.runImport(fileUrl);

        Map<String, Object> result = new HashMap<>();
        result.put("status", "PROCESSING");
        result.put("fileUrl", fileUrl);
        return ResponseEntity.accepted().body(result);
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


    @PostMapping("/verify_sum")
    public ResponseEntity<?> sandboxVerify(@RequestBody SandboxFilter sandboxFilter) throws IOException, NoSuchAlgorithmException {
        // TODO compare check sum prod vs sandbox
        if (sandboxFilter.getCheckSum() == null) return ResponseEntity.badRequest().body("check_sum not is null");
        Map<String, Object> counts = new HashMap<>();
        Path tempFile = Files.createTempFile("workflow_export_", ".json");
        String checksum = generateFileWithInput(counts, tempFile, sandboxFilter);
        Map<String, Object> result = new HashMap<>();
        result.put("counts", counts);
        result.put("checksum_md5", checksum);
        result.put("status", "Export success");
        if (sandboxFilter.getCheckSum().equals(checksum)) {
            result.put("checksum_md5_verify", true);
        } else {
            result.put("checksum_md5_verify", false);
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping("/import")
    public ResponseEntity<Map<String, Object>> sandboxImport(@RequestBody SandboxPut sandboxPut) throws IOException, NoSuchAlgorithmException {
        String fileUrl = sandboxPut.getLink();
        log.debug("API call /sandbox/import, fileUrl={}", fileUrl);

        Path filePath = Files.createTempFile("workflow_import_", ".json");
        try (InputStream in = new URL(fileUrl).openStream()) {
            Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
        String checkSum = calculateChecksum(filePath);
        if (!checkSum.equals(sandboxPut.getCheckSum())) {
            log.debug("calculateChecksum(filePath) {} error. expect {}", checkSum, sandboxPut.getCheckSum());
            return ResponseEntity.badRequest().body(Map.of("status", "ERROR",
                "message", "calculateChecksum(filePath) " + checkSum + " error. expect " + sandboxPut.getCheckSum()));
        }

        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Map<String, Object> result = new HashMap<>();

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
        return ResponseEntity.ok(result);
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

}
