package com.fis.fpt.web.rest.errors;

public class InvalidStatus extends RuntimeException {

    public InvalidStatus(String message) {
        super(message);
    }
}
