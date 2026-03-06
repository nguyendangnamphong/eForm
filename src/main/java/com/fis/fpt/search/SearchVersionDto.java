package com.fis.fpt.search;

import lombok.Data;

@Data
public class SearchVersionDto {
    public String formId;
    public String version;
    public String start;
    public String end;
    public int sort;
}
