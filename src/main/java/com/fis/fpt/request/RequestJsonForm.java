package com.fis.fpt.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
@Data
public class RequestJsonForm {
    @NotBlank
    private String formId;
    private String jsonForm;

}
