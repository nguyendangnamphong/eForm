package com.fis.fpt.request;

import lombok.Data;

@Data
public class RequestShareForm {
    private String orgIn;
    private String email;
    private Long custId;
    private Long userId;

}
