package com.fis.fpt.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
@Data
public class RequestForm {

    @NotBlank
    private String formName;

    @NotBlank
    private String formId;

    @NotNull
    private ZonedDateTime beginTime;

    @NotNull
    private ZonedDateTime endTime;

    @NotBlank
    private String createdBy;

    public String infoFix;


    private String tag;
    private String describeForm;

}
