package com.fis.fpt.web.rest;

import com.fis.fpt.adapter.ZonedDateTimeTypeAdapter;
import com.fis.fpt.domain.Form;
import com.fis.fpt.domain.Version;
import com.fis.fpt.repository.FormRepository;
import com.fis.fpt.request.FormSearchEflow;
import com.fis.fpt.request.RequestAddForm;
import com.fis.fpt.request.RequestListForm;
import com.fis.fpt.search.FormSearchShareDto;
import com.fis.fpt.security.SecurityUtils;
import com.fis.fpt.security.UserInFoDetails;
import com.fis.fpt.service.ExportExcell;
import com.fis.fpt.service.ShareService;
import com.fis.fpt.service.dto.FormShareDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/share")
@RequiredArgsConstructor
public class ShareResource {
    private final ExportExcell exportExcell;
    private final FormRepository formRepository;
    private final ShareService shareService;
    Gson gson = new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeTypeAdapter()).create();

    @PostMapping("/form")
    public ResponseEntity<?> getFormShare(@RequestBody FormSearchShareDto formSearchShareDto, Pageable pageable) throws Exception {
        Page<FormShareDto> formDtos = shareService.getFormShare(formSearchShareDto, pageable);
        return ResponseEntity.ok(formDtos);
    }

    @PostMapping("/duplicate-form")
    public ResponseEntity<?> duplicateForm(@RequestParam String formId, @RequestBody RequestAddForm requestAddForm) throws Exception {
        Form form = shareService.duplicateFormShare(formId, requestAddForm);
        return ResponseEntity.ok(gson.toJson(form));
    }

    @PostMapping("/export-form")
    public ResponseEntity<InputStreamResource> exportForm(FormSearchShareDto formSearchShareDto) throws Exception {
        UserInFoDetails currentUser = SecurityUtils.getInfoCurrentUserLogin();
        ByteArrayInputStream excelData = exportExcell.exportExcelFormShare(formSearchShareDto);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Form.xlsx");

        return ResponseEntity
            .ok()
            .headers(headers)
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .body(new InputStreamResource(excelData));
    }

    @PostMapping("/export-form-list")
    public ResponseEntity<InputStreamResource> exportFormList(@RequestBody RequestListForm requestListForm) throws Exception {
        UserInFoDetails currentUser = SecurityUtils.getInfoCurrentUserLogin();
        ByteArrayInputStream excelData = exportExcell.exportExcelFormShareList(requestListForm.getListFormId());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Form.xlsx");

        return ResponseEntity
            .ok()
            .headers(headers)
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .body(new InputStreamResource(excelData));
    }

    @PostMapping("/form-eflow")
    public ResponseEntity<?> getFormEflow(@RequestBody FormSearchEflow formSearchEflow, Pageable pageable) throws Exception {
        Page<FormShareDto> formDtos = shareService.getFormEflow(formSearchEflow, pageable);
        return ResponseEntity.ok(gson.toJson(formDtos));
    }

    @PostMapping("/version-eflow")
    public ResponseEntity<?> getVersionEflow(@RequestBody List<String> versionIds) throws Exception {
        List<Version> versionList = shareService.getListVersion(versionIds);
        return ResponseEntity.ok(gson.toJson(versionList));
    }

    @GetMapping("/authorize")
    public ResponseEntity<?> getInfoShare(@RequestParam String formId, @RequestParam(required = false) String search) throws Exception {
        return ResponseEntity.ok(shareService.getInfoShare(formId, search));
    }

    @GetMapping("/form-erequest")
    public ResponseEntity<?> getFormErequest(@RequestParam String versionId) throws Exception {
        FormShareDto formDtos = formRepository.findFormErequest(versionId);
        return ResponseEntity.ok(gson.toJson(formDtos));
    }

    @PostMapping("/form-erequests")
    public ResponseEntity<?> getFormErequestList(@RequestBody List<String> versionIds) throws Exception {
        List<FormShareDto> formDtos = formRepository.findFormErequestList(versionIds);
        return ResponseEntity.ok(gson.toJson(formDtos));
    }
}
