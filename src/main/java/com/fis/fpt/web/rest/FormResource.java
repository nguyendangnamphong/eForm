package com.fis.fpt.web.rest;

import com.fis.fpt.domain.Form;
import com.fis.fpt.domain.Version;
import com.fis.fpt.domain.enums.StatusForm;
import com.fis.fpt.repository.FormRepository;
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

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing {@link com.fis.fpt.domain.Form}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FormResource {

    private final Logger log = LoggerFactory.getLogger(FormResource.class);

    private static final String ENTITY_NAME = "eformForm";
    private final VersionRepository versionRepository;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormRepository formRepository;

    public FormResource(FormRepository formRepository, VersionRepository versionRepository) {
        this.formRepository = formRepository;
        this.versionRepository = versionRepository;
    }

    /**
     * {@code POST  /forms} : Create a new form.
     *
     * @param form the form to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new form, or with status {@code 400 (Bad Request)} if the form has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/forms")
    public ResponseEntity<Form> createForm(@RequestBody Form form) throws URISyntaxException {
        log.debug("REST request to save Form : {}", form);
        if (form.getFormId() != null) {
            throw new BadRequestAlertException("A new form cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Form result = formRepository.save(form);
        return ResponseEntity.created(new URI("/api/forms/" + result.getFormId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getFormId().toString()))
            .body(result);
    }

    @PutMapping("/forms/{id}/code")
    public ResponseEntity<String> updateJsonForm(@PathVariable String id, @RequestBody String codeJson) throws URISyntaxException {
        Optional<Form> form = formRepository.findById(id);
        if (form.isEmpty()) {
            throw new BadRequestAlertException("Form not found", ENTITY_NAME, "idnotfound");
        } else {
            Form existingForm = form.get();
            existingForm.setCodeJson(codeJson);
            formRepository.save(existingForm);

            if (existingForm.getStatusForm().equals(StatusForm.EDIT.getValue())){
                Version formVersion = versionRepository.findVersionByFormIdAndActive(existingForm.getFormId(),true);
                formVersion.setCodeJson(codeJson);
                versionRepository.save(formVersion);
            }
            return ResponseEntity.ok()
                .body(existingForm.getCodeJson());
        }
    }

    @GetMapping("/forms/{id}/code")
    public ResponseEntity<?> getJsonForm(@PathVariable String id) {
        log.debug("REST request to get JSON Form : {}", id);
        Optional<Form> form = formRepository.findById(id);
        if (form.isEmpty()) {
            throw new BadRequestAlertException("Form not found", ENTITY_NAME, "idnotfound");
        }
        return ResponseEntity.ok(form.get().getCodeJson());
    }

    /**
     * {@code PUT  /forms} : Updates an existing form.
     *
     * @param form the form to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated form,
     * or with status {@code 400 (Bad Request)} if the form is not valid,
     * or with status {@code 500 (Internal Server Error)} if the form couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/forms")
    public ResponseEntity<Form> updateForm(@RequestBody Form form) throws URISyntaxException {
        log.debug("REST request to update Form : {}", form);
        if (form.getFormId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Form result = formRepository.save(form);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, form.getFormId().toString()))
            .body(result);
    }

    @PatchMapping("/forms/{id}")
    public ResponseEntity<Form> partialUpdateForm(
        @PathVariable String id,
        @RequestBody Map<String, Object> updates
    ) {
        log.debug("REST request to partially update Form with id: {}", id);

        Optional<Form> optionalForm = formRepository.findById(id);
        if (optionalForm.isEmpty()) {
            throw new BadRequestAlertException("Form not found", ENTITY_NAME, "idnotfound");
        }

        Form form = optionalForm.get();

        updates.forEach((key, value) -> {
            try {
                Field field = Form.class.getDeclaredField(key);
                field.setAccessible(true);
                if (value != null) {
                    if (field.getType().equals(ZonedDateTime.class) && value instanceof String) {
                        value = ZonedDateTime.parse((String) value);
                    }
                    field.set(form, value);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new BadRequestAlertException("Invalid field: " + key, ENTITY_NAME, "invalidfield");
            }
        });

        Form result = formRepository.save(form);

        return ResponseEntity.ok()
            .body(result);
    }


    /**
     * {@code GET  /forms} : get all the forms.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of forms in body.
     */
    @GetMapping("/forms")
    public List<Form> getAllForms() {
        log.debug("REST request to get all Forms");
        return formRepository.findAll();
    }

    /**
     * {@code GET  /forms/:id} : get the "id" form.
     *
     * @param id the id of the form to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the form, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/forms/{id}")
    public ResponseEntity<Form> getForm(@PathVariable String id) {
        log.debug("REST request to get Form : {}", id);
        Optional<Form> form = formRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(form);
    }



    /**
     * {@code DELETE  /forms/:id} : delete the "id" form.
     *
     * @param id the id of the form to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/forms/{id}")
    public ResponseEntity<Void> deleteForm(@PathVariable String id) {
        log.debug("REST request to delete Form : {}", id);
        formRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

}
