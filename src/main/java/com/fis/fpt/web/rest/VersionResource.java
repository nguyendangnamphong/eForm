package com.fis.fpt.web.rest;

import com.fis.fpt.domain.Version;
import com.fis.fpt.repository.VersionRepository;
import com.fis.fpt.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.fis.fpt.domain.Version}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VersionResource {

    private final Logger log = LoggerFactory.getLogger(VersionResource.class);

    private static final String ENTITY_NAME = "eformVersion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VersionRepository versionRepository;

    public VersionResource(VersionRepository versionRepository) {
        this.versionRepository = versionRepository;
    }

    /**
     * {@code POST  /versions} : Create a new version.
     *
     * @param version the version to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new version, or with status {@code 400 (Bad Request)} if the version has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/versions")
    public ResponseEntity<Version> createVersion(@RequestBody Version version) throws URISyntaxException {
        log.debug("REST request to save Version : {}", version);
        if (version.getVersionId() != null) {
            throw new BadRequestAlertException("A new version cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Version result = versionRepository.save(version);
        return ResponseEntity.created(new URI("/api/versions/" + result.getVersionId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getVersionId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /versions} : Updates an existing version.
     *
     * @param version the version to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated version,
     * or with status {@code 400 (Bad Request)} if the version is not valid,
     * or with status {@code 500 (Internal Server Error)} if the version couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/versions")
    public ResponseEntity<Version> updateVersion(@RequestBody Version version) throws URISyntaxException {
        log.debug("REST request to update Version : {}", version);
        if (version.getVersionId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Version result = versionRepository.save(version);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, version.getVersionId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /versions} : get all the versions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of versions in body.
     */
    @GetMapping("/versions")
    public List<Version> getAllVersions() {
        log.debug("REST request to get all Versions");
        return versionRepository.findAll();
    }

    /**
     * {@code GET  /versions/:id} : get the "id" version.
     *
     * @param id the id of the version to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the version, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/versions/{id}")
    public ResponseEntity<Version> getVersion(@PathVariable String id) {
        log.debug("REST request to get Version : {}", id);
        Optional<Version> version = versionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(version);
    }

    /**
     * {@code DELETE  /versions/:id} : delete the "id" version.
     *
     * @param id the id of the version to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/versions/{id}")
    public ResponseEntity<Void> deleteVersion(@PathVariable String id) {
        log.debug("REST request to delete Version : {}", id);
        versionRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
