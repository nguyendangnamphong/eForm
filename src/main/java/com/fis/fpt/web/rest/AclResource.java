package com.fis.fpt.web.rest;

import com.fis.fpt.domain.Acl;
import com.fis.fpt.repository.AclRepository;
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
 * REST controller for managing {@link com.fis.fpt.domain.Acl}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AclResource {

    private final Logger log = LoggerFactory.getLogger(AclResource.class);

    private static final String ENTITY_NAME = "eformAcl";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AclRepository aclRepository;

    public AclResource(AclRepository aclRepository) {
        this.aclRepository = aclRepository;
    }

    /**
     * {@code POST  /acls} : Create a new acl.
     *
     * @param acl the acl to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new acl, or with status {@code 400 (Bad Request)} if the acl has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/acls")
    public ResponseEntity<Acl> createAcl(@RequestBody Acl acl) throws URISyntaxException {
        log.debug("REST request to save Acl : {}", acl);
        if (acl.getIdAcl() != null) {
            throw new BadRequestAlertException("A new acl cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Acl result = aclRepository.save(acl);
        return ResponseEntity.created(new URI("/api/acls/" + result.getIdAcl()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getIdAcl().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /acls} : Updates an existing acl.
     *
     * @param acl the acl to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated acl,
     * or with status {@code 400 (Bad Request)} if the acl is not valid,
     * or with status {@code 500 (Internal Server Error)} if the acl couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/acls")
    public ResponseEntity<Acl> updateAcl(@RequestBody Acl acl) throws URISyntaxException {
        log.debug("REST request to update Acl : {}", acl);
        if (acl.getIdAcl() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Acl result = aclRepository.save(acl);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, acl.getIdAcl().toString()))
            .body(result);
    }

    /**
     * {@code GET  /acls} : get all the acls.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of acls in body.
     */
    @GetMapping("/acls")
    public List<Acl> getAllAcls() {
        log.debug("REST request to get all Acls");
        return aclRepository.findAll();
    }

    /**
     * {@code GET  /acls/:id} : get the "id" acl.
     *
     * @param id the id of the acl to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the acl, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/acls/{id}")
    public ResponseEntity<Acl> getAcl(@PathVariable String id) {
        log.debug("REST request to get Acl : {}", id);
        Optional<Acl> acl = aclRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(acl);
    }

    /**
     * {@code DELETE  /acls/:id} : delete the "id" acl.
     *
     * @param id the id of the acl to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/acls/{id}")
    public ResponseEntity<Void> deleteAcl(@PathVariable String id) {
        log.debug("REST request to delete Acl : {}", id);
        aclRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
