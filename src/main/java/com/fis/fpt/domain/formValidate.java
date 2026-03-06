package com.fis.fpt.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class formValidate implements Serializable {
    private String minLength;
    private String maxLength;
    private String regex;
    private List<String> listSelectedRegex;
    private String messErrorForRegex;
    private Boolean required;
    private Boolean unique;
    private String decimalPlace;
    private Boolean requireDecimal;
    private Boolean mutilChoose;
    private String minDate;
    private String maxDate;
    private String formatDate;
    private String inputMask;


}
