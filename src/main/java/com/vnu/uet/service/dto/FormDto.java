package com.vnu.uet.service.dto;

import com.vnu.uet.domain.Form;
import lombok.Data;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Data
public class FormDto {
    private Long custId;
    private String orgIn;
    private Long userId;
    private String createdBy;
    private String jsonForm;
    private String formId;

    private String formName;

    private String statusForm;

    private String createdDate;
    private String updateAt;

    private String[] tag = {};

    private String beginTime;

    private String endTime;

    private String listProcedure;

    private String describeForm;
    private String jsonFormCondition;

    private Boolean fix = false;
    private Boolean duplicate = true;
    private Boolean stopRelease = true;
    private Boolean release = false;
    private Boolean extendValidity = true;
    String versionId;
    private String formCode;
    private String codeJson;
    private String configWriter;

    public FormDto(String formId, String formName, Long statusForm, Instant createdDate, String tag, Instant beginTime,
            Instant endTime, Instant updateAt, String jsonForm, String describeForm, String formCode,
            String jsonFormCondition, String createdBy, Long userId, String orgIn, Long custId, String codeJson,
            String configWriter) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.of("Asia/Ho_Chi_Minh"));
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.of("Asia/Ho_Chi_Minh"));
        this.formName = formName;
        if (statusForm == 1L) {
            this.statusForm = "draft";
        } else if (statusForm == 2L) {
            this.statusForm = "releasing";
        } else if (statusForm == 4L) {
            this.statusForm = "editing";
        } else if (statusForm == 3L) {
            this.statusForm = "stop release";
        }
        this.formCode = formCode;
        this.createdDate = formatter.format(createdDate);
        if (!tag.equals("")) {
            this.tag = tag.split(",");
        }
        this.userId = userId;
        this.orgIn = orgIn;
        this.custId = custId;
        this.createdBy = createdBy;
        this.beginTime = formatter.format(beginTime);
        this.endTime = formatter.format(endTime);
        this.formId = formId;
        this.updateAt = formatter1.format(updateAt);
        this.jsonForm = jsonForm;
        this.describeForm = describeForm;
        this.jsonFormCondition = jsonFormCondition;
        this.codeJson = codeJson;
        this.configWriter = configWriter;
        Instant currentTime = Instant.now();
        if (statusForm == 1 || statusForm == 3 || statusForm == 4) {
            this.fix = true;
            this.stopRelease = false;
            if (currentTime.isBefore(endTime)) {
                this.release = true;
            }
            // In ra thời gian hiện tại
        }
        if (statusForm == 3) {
            this.extendValidity = false;
        }

    }

    public FormDto(String formId, String formName, Long statusForm, Instant createdDate, String tag, Instant beginTime,
            Instant endTime, Instant updateAt, String describeForm, String formCode, String createdBy, Long userId,
            String orgIn, Long custId, String configWriter) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.of("Asia/Ho_Chi_Minh"));
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.of("Asia/Ho_Chi_Minh"));
        this.formName = formName;
        if (statusForm == 1L) {
            this.statusForm = "draft";
        } else if (statusForm == 2L) {
            this.statusForm = "releasing";
        } else if (statusForm == 4L) {
            this.statusForm = "editing";
        } else if (statusForm == 3L) {
            this.statusForm = "stop release";
        }
        this.formCode = formCode;
        this.createdDate = formatter.format(createdDate);
        if (!tag.equals("")) {
            this.tag = tag.split(",");
        }
        this.userId = userId;
        this.orgIn = orgIn;
        this.custId = custId;
        this.createdBy = createdBy;
        this.beginTime = formatter.format(beginTime);
        this.endTime = formatter.format(endTime);
        this.formId = formId;
        this.updateAt = formatter1.format(updateAt);
        this.describeForm = describeForm;
        this.configWriter = configWriter;
        Instant currentTime = Instant.now();
        if (statusForm == 1 || statusForm == 3 || statusForm == 4) {
            this.fix = true;
            this.stopRelease = false;
            if (currentTime.isBefore(endTime)) {
                this.release = true;
            }
            // In ra thời gian hiện tại
        }
        if (statusForm == 3) {
            this.extendValidity = false;
        }

    }

    public FormDto() {
    }

    public static FormDto formTODto(Form form) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.of("Asia/Ho_Chi_Minh"));
        FormDto formDto = new FormDto();
        formDto.formId = form.getFormId();
        formDto.formName = form.getFormName();
        if (form.getStatusForm() == 1L) {
            formDto.statusForm = "draff";
        } else if (form.getStatusForm() == 2L) {
            formDto.statusForm = "releasing";
        } else if (form.getStatusForm() == 4L) {
            formDto.statusForm = "editing";
        } else {
            formDto.statusForm = "stop release";
        }
        formDto.userId = form.getUserId();
        formDto.custId = form.getCustId();
        formDto.orgIn = form.getOrgIn();
        formDto.createdBy = form.getCreatedBy();
        formDto.createdDate = formatter.format(form.getCreatedDate());
        if (form.getTag() != null && !form.getTag().isEmpty()) {
            formDto.tag = form.getTag().split(",");
        }
        formDto.beginTime = formatter.format(form.getBeginTime());
        formDto.endTime = formatter.format(form.getEndTime());
        formDto.updateAt = formatter.format(form.getLastModifiedDate());
        formDto.jsonForm = form.getJsonForm();
        formDto.describeForm = form.getDescription();
        formDto.formCode = form.getFormCode();
        formDto.jsonFormCondition = form.getJsonFormCondition();
        formDto.codeJson = form.getCodeJson();
        formDto.configWriter = form.getConfigWriter();
        ZoneId zoneId = ZoneId.systemDefault();

        Instant currentTime = Instant.now();
        if (Objects.equals(form.getStatusForm(), 1L) || Objects.equals(form.getStatusForm(), 3L)
                || Objects.equals(form.getStatusForm(), 4L)) {
            formDto.fix = true;
            formDto.stopRelease = false;
            if (currentTime.isBefore(form.getEndTime())) {
                formDto.release = true;
            }
        }
        if (Objects.equals(form.getStatusForm(), 3L)) {
            formDto.extendValidity = false;
        }
        return formDto;
    }

}
