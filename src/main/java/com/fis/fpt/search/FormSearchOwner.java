package com.fis.fpt.search;

import com.fis.fpt.converter.DateConverter;
import com.fis.fpt.domain.enums.StatusForm;
import com.fis.fpt.security.SecurityUtils;
import com.fis.fpt.security.UserInFoDetails;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class FormSearchOwner {
    @NotBlank
    private String createdBy;
    private String tag;
    private List<Long> statusFormList = Collections.emptyList();
    private String formName;
    private Instant beginDate;
    private Instant endDate;
    private Instant beginTime;
    private Instant endTime;

    public FormSearchOwner(FormSearchOwnerDto formSearchOwnerDto) {
        UserInFoDetails currentUser = SecurityUtils.getInfoCurrentUserLogin();
        this.createdBy = currentUser.getLogin().trim();
        this.formName = formSearchOwnerDto.getFormName();
        this.tag = formSearchOwnerDto.getTag();
        List<String> rawStatuses = formSearchOwnerDto.getStatusForm();
        if (rawStatuses != null && !rawStatuses.isEmpty()) {
            this.statusFormList = rawStatuses.stream()
                .map(String::trim)
                .map(this::mapStatusStringToLong)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        }
        if (!(formSearchOwnerDto.getBeginTime() == null || formSearchOwnerDto.getBeginTime().equals(""))) {
            this.beginTime = DateConverter.parseStringToZonedDateTime(formSearchOwnerDto.getBeginTime());
        }
        if (!(formSearchOwnerDto.getEndTime() == null || formSearchOwnerDto.getEndTime().equals(""))) {
            this.endTime = DateConverter.parseStringToZonedDateTime3(formSearchOwnerDto.getEndTime());
        }
        if (!(formSearchOwnerDto.getBeginDate() == null || formSearchOwnerDto.getBeginDate().equals(""))) {
            this.beginDate = DateConverter.parseStringToZonedDateTime(formSearchOwnerDto.getBeginDate());
        }
        if (!(formSearchOwnerDto.getEndDate() == null || formSearchOwnerDto.getEndDate().equals(""))) {
            this.endDate = DateConverter.parseStringToZonedDateTime3(formSearchOwnerDto.getEndDate());
        }
    }

    private Long mapStatusStringToLong(String statusStr) {
        switch (statusStr) {
            case "draft": {
                return StatusForm.DRAFT.getValue();
            }
            case "releasing": {
                return StatusForm.RELEASE.getValue();
            }
            case "stop release": {
                return StatusForm.WITHDRAW.getValue();
            }
            case "editing": {
                return StatusForm.EDIT.getValue();
            }
            default: {
                return null;
            }
        }
    }

}
