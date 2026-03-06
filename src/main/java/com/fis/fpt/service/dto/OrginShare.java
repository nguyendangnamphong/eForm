package com.fis.fpt.service.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class OrginShare {
    public Long numberMember;
    public List<InfoOrginShare> data = new ArrayList<>();
}

