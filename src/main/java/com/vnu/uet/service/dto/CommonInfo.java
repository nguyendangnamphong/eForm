package com.vnu.uet.service.dto;

import com.vnu.uet.domain.Form;
import com.vnu.uet.domain.Version;
import lombok.Data;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
@Data
public class CommonInfo {
    public String formCode;
    public String formName;
    public String formId;
    public String beginTime;
    public String endTime;
    public String describeForm;
    public String createdDate;
    public Long statusForm;
    public String updateAt;
    public String[] tag = {};
    public String jsonForm;
    public String variableArr;
    public String jsonFormCondition;
    private String codeJson;
    private String configWriter;
    private String versionId;

    public CommonInfo(Form form) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Ho_Chi_Minh"));;
        this.formName = form.getFormName();
        this.formId = form.getFormId();
        this.describeForm = form.getDescription();
        if(!(form.getTag().equals(""))) {
            this.tag = form.getTag().split(",");
        }
        this.formCode = form.getFormCode();
        this.beginTime = formatter.format(form.getBeginTime());
        this.endTime = formatter.format(form.getEndTime());
        this.jsonForm = form.getJsonForm();
        this.statusForm = form.getStatusForm();
        this.updateAt = formatter.format(form.getLastModifiedDate());
        this.createdDate = formatter.format(form.getCreatedDate());
        this.variableArr = form.getVariableArr();
        this.jsonFormCondition = form.getJsonFormCondition();
        this.codeJson = form.getCodeJson();
        this.configWriter = form.getConfigWriter();

    }

    public CommonInfo(Form form, String versionId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Ho_Chi_Minh"));;
        this.formName = form.getFormName();
        this.formId = form.getFormId();
        this.describeForm = form.getDescription();
        if(!(form.getTag().equals(""))) {
            this.tag = form.getTag().split(",");
        }
        this.formCode = form.getFormCode();
        this.beginTime = formatter.format(form.getBeginTime());
        this.endTime = formatter.format(form.getEndTime());
        this.jsonForm = form.getJsonForm();
        this.statusForm = form.getStatusForm();
        this.updateAt = formatter.format(form.getLastModifiedDate());
        this.createdDate = formatter.format(form.getCreatedDate());
        this.variableArr = form.getVariableArr();
        this.jsonFormCondition = form.getJsonFormCondition();
        this.codeJson = form.getCodeJson();
        this.configWriter = form.getConfigWriter();
        this.versionId = versionId;

    }

    public CommonInfo(Version version) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Ho_Chi_Minh"));;
        this.formName = version.getFormName();
        this.formId = version.getFormId();
        this.describeForm = version.getDescription();
        if(!(version.getTag().equals(""))) {
            this.tag = version.getTag().split(",");
        }
        this.formCode = version.getFormCode();
        this.beginTime = formatter.format(version.getBeginTime());
        this.endTime = formatter.format(version.getEndTime());
        this.jsonForm = version.getJsonForm();
        this.statusForm = version.getStatusForm();
        this.updateAt = formatter.format(version.getLastModifiedDate());
        this.createdDate = formatter.format(version.getCreatedDate());
        this.variableArr = version.getVariableArr();
        this.jsonFormCondition = version.getJsonFormCondition();
        this.codeJson = version.getCodeJson();
        this.configWriter = version.getConfigWriter();
        this.versionId = version.getVersionId();
    }
}
