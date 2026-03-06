package com.fis.fpt.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum Perm {
    OWNER("o"),
    EDIT("e"),
    VIEW("v"),
    PUBLIC_VIEW("p_v");

    private String value;

    private static final Map<String, Perm> map = new HashMap<>();

    static {
        for (Perm perm : values()) {
            map.put(perm.value, perm);
        }
    }

    public static Perm findKey(String value){
        return map.get(value);
    }
}
