package com.fis.fpt.request;

import lombok.Data;

@Data
public class RequestDeleteShare {
    private String orgIn;
    private String email;
    private String formId;
}
