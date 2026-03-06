package com.fis.fpt.service.dto;

import lombok.Data;

@Data
public class ReceiverDTO {

    private String email;
    private Long userId;
    private Long custId;
    private String orgIn;
}

