package com.fis.fpt.service.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class InfoOrginShare {
    public String orgName;
    public List<AccountInfo> listMember = new ArrayList<>();

}
