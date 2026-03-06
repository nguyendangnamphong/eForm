package com.fis.fpt.web.rest;

import com.fis.fpt.domain.VariableSystemAcl;
import com.fis.fpt.repository.VariableSystemAclRepository;
import com.fis.fpt.service.VariableSystemAclService;
import com.fis.fpt.service.dto.StandardResponse;
import com.fis.fpt.service.dto.VariableSystemAclDTO;
import com.fis.fpt.service.dto.VariableSystemAclRequest;
import io.github.jhipster.web.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.fis.fpt.domain.VariableSystemAcl}.
 */
@RestController
@RequestMapping("/api")
@Transactional
@RequiredArgsConstructor
@Log4j2
public class VariableSystemAclResource {

    private final VariableSystemAclService variableSystemAclService;
    private final VariableSystemAclRepository variableSystemAclRepository;

    @PostMapping("/variable-system-acls")
    public ResponseEntity<StandardResponse> createVariableSystemAcl(@RequestParam String varId, @RequestBody List<VariableSystemAclRequest> variableSystemAclRequests) throws URISyntaxException {
        log.debug("REST request to save VariableSystemAcl : {}", varId);

        StandardResponse response = variableSystemAclService.createVariableSystemAcl(varId, variableSystemAclRequests);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/variable-system-acls")
    public ResponseEntity<StandardResponse> updateVariableSystemAcl(@RequestBody List<VariableSystemAclDTO> variableSystemAcls) throws URISyntaxException {
        log.debug("REST request to update VariableSystemAcl : {}", variableSystemAcls);

        StandardResponse response = variableSystemAclService.updateVariableSystemAcl(variableSystemAcls);
        return ResponseEntity.ok(response);
    }

    /**
     * {@code GET  /variable-system-acls} : get all the variableSystemAcls.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of variableSystemAcls in body.
     */
    @GetMapping("/variable-system-acls")
    public ResponseEntity<StandardResponse> getAllVariableSystemAcls(@RequestParam String varId) {
        log.debug("REST request to get all VariableSystemAcls");
        StandardResponse response = variableSystemAclService.getAllVariableSystemAcls(varId);
        return ResponseEntity.ok(response);
    }

    /**
     * {@code GET  /variable-system-acls/:id} : get the "id" variableSystemAcl.
     *
     * @param id the id of the variableSystemAcl to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the variableSystemAcl, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/variable-system-acls/{id}")
    public ResponseEntity<VariableSystemAcl> getVariableSystemAcl(@PathVariable Long id) {
        log.debug("REST request to get VariableSystemAcl : {}", id);
        Optional<VariableSystemAcl> variableSystemAcl = variableSystemAclRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(variableSystemAcl);
    }


    @DeleteMapping("/variable-system-acls")
    public ResponseEntity<StandardResponse> deleteVariableSystemAclList(@RequestBody List<String> ids) {
        log.debug("REST request to delete VariableSystemAcl : {}", ids);
        StandardResponse response = variableSystemAclService.deleteVariableSystemAclList(ids);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/variable-system-acls/{id}")
    public ResponseEntity<StandardResponse> deleteVariableSystemAcl(@PathVariable("id") String id) {
        log.debug("REST request to delete VariableSystemAcl: {}", id);
        StandardResponse response = variableSystemAclService.deleteVariableSystemAcl(id);
        return ResponseEntity.ok(response);
    }
}
