package com.fis.fpt.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum PermType {
    USER(0L),
    ORG(1L),
    GROUP(2L),
    CUST(3L),
    USER_EXTERNAL(4L),
    ROLE(5L);

    private final Long value;

    private static final Map<Long, PermType> map = new HashMap<>();

    static {
        for (PermType status : values()) {
            map.put(status.value, status);
        }
    }

    public static PermType findKey(Long value){
        return map.get(value);
    }
}
