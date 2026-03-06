package com.fis.fpt.web.rest;

import com.fis.fpt.domain.VariableSystem;
import com.fis.fpt.repository.VariableSystemRepository;
import com.fis.fpt.security.SecurityUtils;
import com.fis.fpt.security.UserInFoDetails;
import com.fis.fpt.service.VariableSystemService;
import com.fis.fpt.service.dto.StandardResponse;
import com.fis.fpt.service.dto.VariableSystemDTO;
import com.fis.fpt.service.dto.VariableSystemRequest;
import com.fis.fpt.service.dto.VariableSystemUpdateRequest;
import com.fis.fpt.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.fis.fpt.domain.VariableSystem}.
 */
@RestController
@RequestMapping("/api")
@Transactional
@RequiredArgsConstructor
@Log4j2
public class VariableSystemResource {

    private final VariableSystemService variableSystemService;


    @PostMapping("/variable-systems")
    public ResponseEntity<StandardResponse> createVariableSystem(@RequestBody VariableSystemRequest variableSystemRequest) throws URISyntaxException {
        log.debug("REST request to save VariableSystem : {}", variableSystemRequest);
        StandardResponse standardResponse = variableSystemService.createVariableSystem(variableSystemRequest.getVariableSystem());
        return ResponseEntity.ok(standardResponse);
    }


    @GetMapping("/variable-systems/share")
    public ResponseEntity<StandardResponse> getVariableSystemShare() throws URISyntaxException {
        StandardResponse response = variableSystemService.getVariableSystemsShare();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/variable-systems/{id}")
    public ResponseEntity<StandardResponse> getVariableSystemsById(@PathVariable String id) throws URISyntaxException {
        StandardResponse response = variableSystemService.getVariableSystemById(id);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/variable-systems")
    public ResponseEntity<StandardResponse> updateVariableSystem(@RequestBody VariableSystemUpdateRequest variableSystemUpdateRequest) {
        log.debug("REST request to update VariableSystem : {}", variableSystemUpdateRequest);

        StandardResponse resultDto = variableSystemService.updateVariableSystem(variableSystemUpdateRequest);

        return ResponseEntity.ok(resultDto);
    }

    @GetMapping("/variable-systems")
    public ResponseEntity<StandardResponse> getAllVariableSystems() {
        log.debug("REST request to get all VariableSystems");
        StandardResponse response = variableSystemService.getAllVariableSystems();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/variable-systems/{id}")
    public ResponseEntity<StandardResponse> deleteVariableSystem(@PathVariable String id) {
        log.debug("REST request to delete VariableSystem : {}", id);
        StandardResponse response = variableSystemService.deleteVariableSystem(id);
        return ResponseEntity.ok(response);
    }
}
