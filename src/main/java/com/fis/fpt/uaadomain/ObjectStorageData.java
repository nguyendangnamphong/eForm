package com.fis.fpt.uaadomain;

import java.io.Serializable;

public class ObjectStorageData implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -7335289986380398546L;
    private Long id;
    private String type;
    private Boolean primary;

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static enum Type {

        ORIGINAL("original"), METABOLIC("metabolic");

        private String value;

        Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

    }

}

