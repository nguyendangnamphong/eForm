package com.fis.fpt.web.rest;

import com.fis.fpt.domain.ApiInfo;
import com.fis.fpt.repository.ApiInfoRepository;
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
 * REST controller for managing {@link com.fis.fpt.domain.ApiInfo}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ApiInfoResource {

    private final Logger log = LoggerFactory.getLogger(ApiInfoResource.class);

    private static final String ENTITY_NAME = "eformApiInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApiInfoRepository apiInfoRepository;

    public ApiInfoResource(ApiInfoRepository apiInfoRepository) {
        this.apiInfoRepository = apiInfoRepository;
    }

    /**
     * {@code POST  /api-infos} : Create a new apiInfo.
     *
     * @param apiInfo the apiInfo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new apiInfo, or with status {@code 400 (Bad Request)} if the apiInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/api-infos")
    public ResponseEntity<ApiInfo> createApiInfo(@RequestBody ApiInfo apiInfo) throws URISyntaxException {
        log.debug("REST request to save ApiInfo : {}", apiInfo);
        if (apiInfo.getId() != null) {
            throw new BadRequestAlertException("A new apiInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApiInfo result = apiInfoRepository.save(apiInfo);
        return ResponseEntity.created(new URI("/api/api-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /api-infos} : Updates an existing apiInfo.
     *
     * @param apiInfo the apiInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apiInfo,
     * or with status {@code 400 (Bad Request)} if the apiInfo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the apiInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/api-infos")
    public ResponseEntity<ApiInfo> updateApiInfo(@RequestBody ApiInfo apiInfo) throws URISyntaxException {
        log.debug("REST request to update ApiInfo : {}", apiInfo);
        if (apiInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ApiInfo result = apiInfoRepository.save(apiInfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, apiInfo.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /api-infos} : get all the apiInfos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of apiInfos in body.
     */
    @GetMapping("/api-infos")
    public List<ApiInfo> getAllApiInfos() {
        log.debug("REST request to get all ApiInfos");
        return apiInfoRepository.findAll();
    }

    /**
     * {@code GET  /api-infos/:id} : get the "id" apiInfo.
     *
     * @param id the id of the apiInfo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the apiInfo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/api-infos/{id}")
    public ResponseEntity<ApiInfo> getApiInfo(@PathVariable String id) {
        log.debug("REST request to get ApiInfo : {}", id);
        Optional<ApiInfo> apiInfo = apiInfoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(apiInfo);
    }

    /**
     * {@code DELETE  /api-infos/:id} : delete the "id" apiInfo.
     *
     * @param id the id of the apiInfo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/api-infos/{id}")
    public ResponseEntity<Void> deleteApiInfo(@PathVariable String id) {
        log.debug("REST request to delete ApiInfo : {}", id);
        apiInfoRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
