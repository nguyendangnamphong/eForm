package com.fis.fpt.uaadomain;

import java.io.Serializable;

public class TextLabel implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 777894177297222251L;
    public String value ;
    public Boolean isLabel;
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public Boolean getIsLabel() {
        return isLabel;
    }
    public void setIsLabel(Boolean isLabel) {
        this.isLabel = isLabel;
    }


}
