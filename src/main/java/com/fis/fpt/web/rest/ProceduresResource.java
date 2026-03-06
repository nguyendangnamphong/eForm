package com.fis.fpt.web.rest;

import com.fis.fpt.domain.Procedures;
import com.fis.fpt.repository.ProceduresRepository;
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
 * REST controller for managing {@link com.fis.fpt.domain.Procedures}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProceduresResource {

    private final Logger log = LoggerFactory.getLogger(ProceduresResource.class);

    private static final String ENTITY_NAME = "eformProcedures";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProceduresRepository proceduresRepository;

    public ProceduresResource(ProceduresRepository proceduresRepository) {
        this.proceduresRepository = proceduresRepository;
    }

    /**
     * {@code POST  /procedures} : Create a new procedures.
     *
     * @param procedures the procedures to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new procedures, or with status {@code 400 (Bad Request)} if the procedures has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/procedures")
    public ResponseEntity<Procedures> createProcedures(@RequestBody Procedures procedures) throws URISyntaxException {
        log.debug("REST request to save Procedures : {}", procedures);
        if (procedures.getId() != null) {
            throw new BadRequestAlertException("A new procedures cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Procedures result = proceduresRepository.save(procedures);
        return ResponseEntity.created(new URI("/api/procedures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /procedures} : Updates an existing procedures.
     *
     * @param procedures the procedures to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated procedures,
     * or with status {@code 400 (Bad Request)} if the procedures is not valid,
     * or with status {@code 500 (Internal Server Error)} if the procedures couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/procedures")
    public ResponseEntity<Procedures> updateProcedures(@RequestBody Procedures procedures) throws URISyntaxException {
        log.debug("REST request to update Procedures : {}", procedures);
        if (procedures.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Procedures result = proceduresRepository.save(procedures);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, procedures.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /procedures} : get all the procedures.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of procedures in body.
     */
    @GetMapping("/procedures")
    public List<Procedures> getAllProcedures() {
        log.debug("REST request to get all Procedures");
        return proceduresRepository.findAll();
    }

    /**
     * {@code GET  /procedures/:id} : get the "id" procedures.
     *
     * @param id the id of the procedures to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the procedures, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/procedures/{id}")
    public ResponseEntity<Procedures> getProcedures(@PathVariable String id) {
        log.debug("REST request to get Procedures : {}", id);
        Optional<Procedures> procedures = proceduresRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(procedures);
    }

    /**
     * {@code DELETE  /procedures/:id} : delete the "id" procedures.
     *
     * @param id the id of the procedures to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/procedures/{id}")
    public ResponseEntity<Void> deleteProcedures(@PathVariable String id) {
        log.debug("REST request to delete Procedures : {}", id);
        proceduresRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
