package com.fis.fpt.web.rest;

import com.fis.fpt.domain.StorageFile;
import com.fis.fpt.repository.StorageFileRepository;
import com.fis.fpt.service.S3Service;
import com.fis.fpt.service.dto.PresignedUrlResponse;
import com.fis.fpt.service.dto.StorageFileDTO;
import com.fis.fpt.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.fis.fpt.domain.StorageFile}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class StorageFileResource {

    private final Logger log = LoggerFactory.getLogger(StorageFileResource.class);

    private static final String ENTITY_NAME = "eformStorageFile";
    private final S3Service s3Service;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StorageFileRepository storageFileRepository;

    public StorageFileResource(StorageFileRepository storageFileRepository, S3Service s3Service) {
        this.storageFileRepository = storageFileRepository;
        this.s3Service = s3Service;
    }

    /**
     * {@code POST  /storage-files} : Create a new storageFile.
     *
     * @param storageFile the storageFile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new storageFile, or with status {@code 400 (Bad Request)} if the storageFile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/storage-files")
    public ResponseEntity<StorageFile> createStorageFile(@RequestBody StorageFile storageFile) throws URISyntaxException {
        log.debug("REST request to save StorageFile : {}", storageFile);
        if (storageFile.getId() != null) {
            throw new BadRequestAlertException("A new storageFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StorageFile result = storageFileRepository.save(storageFile);
        return ResponseEntity.created(new URI("/api/storage-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /storage-files} : Updates an existing storageFile.
     *
     * @param storageFile the storageFile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storageFile,
     * or with status {@code 400 (Bad Request)} if the storageFile is not valid,
     * or with status {@code 500 (Internal Server Error)} if the storageFile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/storage-files")
    public ResponseEntity<StorageFile> updateStorageFile(@RequestBody StorageFile storageFile) throws URISyntaxException {
        log.debug("REST request to update StorageFile : {}", storageFile);
        if (storageFile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StorageFile result = storageFileRepository.save(storageFile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, storageFile.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /storage-files} : get all the storageFiles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of storageFiles in body.
     */
    @GetMapping("/storage-files")
    public List<StorageFile> getAllStorageFiles() {
        log.debug("REST request to get all StorageFiles");
        return storageFileRepository.findAll();
    }

    /**
     * {@code GET  /storage-files/:id} : get the "id" storageFile.
     *
     * @param id the id of the storageFile to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the storageFile, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/storage-files/{id}")
    public ResponseEntity<StorageFile> getStorageFile(@PathVariable String id) {
        log.debug("REST request to get StorageFile : {}", id);
        Optional<StorageFile> storageFile = storageFileRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(storageFile);
    }

    /**
     * {@code DELETE  /storage-files/:id} : delete the "id" storageFile.
     *
     * @param id the id of the storageFile to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/storage-files/{id}")
    public ResponseEntity<Void> deleteStorageFile(@PathVariable String id) {
        log.debug("REST request to delete StorageFile : {}", id);
        storageFileRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/storage-files/downloadFile")
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
}



