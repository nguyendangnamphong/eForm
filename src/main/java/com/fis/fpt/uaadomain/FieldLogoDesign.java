package com.fis.fpt.uaadomain;

import java.io.Serializable;

public class FieldLogoDesign implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4835928941117153918L;

    private String position = FieldDesign.POSITION.LEFT.toString();

    private Integer size = 80;

    private Boolean required = true;

    private String value;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
