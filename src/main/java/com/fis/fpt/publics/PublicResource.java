package com.fis.fpt.publics;

import com.fis.fpt.domain.Form;
import com.fis.fpt.repository.FormRepository;
import com.fis.fpt.service.CommonService;
import com.fis.fpt.service.dto.CommonInfo;
import com.fis.fpt.service.dto.FormShareDto;
import com.fis.fpt.web.rest.errors.BadRequestAlertException;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/p/api")
@Transactional
@Log4j2
public class PublicResource {

    private final FormRepository formRepository;

    private static final String ENTITY_NAME = "eformForm";
    private final CommonService commonService;
    private final Gson gson;

    public PublicResource(FormRepository formRepository, CommonService commonService, Gson gson) {
        this.formRepository = formRepository;
        this.commonService = commonService;
        this.gson = gson;
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

    @GetMapping("/common/form")
    public ResponseEntity<?> previewForm(@RequestParam(required = false) String versionId, @RequestParam(required = false) String formId) throws Exception {
        CommonInfo commonInfo = commonService.getCommonInfo(versionId, formId);
        return ResponseEntity.ok(gson.toJson(commonInfo));
    }

    @GetMapping("/share/form-erequest")
    public ResponseEntity<?> getFormErequest(@RequestParam String versionId) throws Exception {
        FormShareDto formDtos = formRepository.findFormErequest(versionId);
        return ResponseEntity.ok(gson.toJson(formDtos));
    }
}
