package com.fis.fpt.request;

import lombok.Data;

import java.util.Map;

@Data
public class RequestApi {
    private String url;
    private String path;
    private Map<String, String> headers;
    private Object payload;
    private String method;
}
