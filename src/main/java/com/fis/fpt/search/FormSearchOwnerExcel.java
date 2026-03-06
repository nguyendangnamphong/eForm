package com.fis.fpt.search;

import com.fis.fpt.converter.DateConverter;
import com.fis.fpt.domain.enums.StatusForm;
import com.fis.fpt.security.SecurityUtils;
import com.fis.fpt.security.UserInFoDetails;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Data
public class FormSearchOwnerExcel {
    @NotBlank
    private String createdBy;
    private String tag;
    private Long statusForm;
    private String formName;
    private Instant beginDate;
    private Instant endDate;
    private Instant beginTime;
    private Instant endTime;

    public FormSearchOwnerExcel(FormSearchOwnerExcelDto formSearchOwnerDto) {
        UserInFoDetails currentUser = SecurityUtils.getInfoCurrentUserLogin();
        this.createdBy = currentUser.getLogin().trim();
        this.formName = formSearchOwnerDto.getFormName();
        this.tag = formSearchOwnerDto.getTag();
        if (formSearchOwnerDto.getStatusForm() != null) {
            switch (formSearchOwnerDto.getStatusForm()) {
                case "draft":
                    this.statusForm = StatusForm.DRAFT.getValue();
                    break;
                case "releasing":
                    this.statusForm = StatusForm.RELEASE.getValue();
                    break;
                case "stop release":
                    this.statusForm = StatusForm.WITHDRAW.getValue();
                    break;
                case "editing":
                    this.statusForm = StatusForm.EDIT.getValue();
                    break;
                default:
                    this.statusForm = null;
            }
        } else {
            this.statusForm = null;
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

}
