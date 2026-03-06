package com.fis.fpt.uaadomain;

import java.io.Serializable;

public class FieldDesign implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7241592294581857294L;

    private String id;
    private String type;
    private Integer w;
    private Integer h;

    private FieldLogoDesign fieldLogoDesign;

    private FieldTextDesign fieldTextDesign;

    public Integer getW() {
        return w;
    }
    public void setW(Integer w) {
        this.w = w;
    }

    public Integer getH() {
        return h;
    }

    public void setH(Integer h) {
        this.h = h;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FieldLogoDesign getFieldLogoDesign() {
        return fieldLogoDesign;
    }

    public void setFieldLogoDesign(FieldLogoDesign fieldLogoDesign) {
        this.fieldLogoDesign = fieldLogoDesign;
    }

    public FieldTextDesign getFieldTextDesign() {
        return fieldTextDesign;
    }

    public void setFieldTextDesign(FieldTextDesign fieldTextDesign) {
        this.fieldTextDesign = fieldTextDesign;
    }

    public static enum POSITION {
        TOP("T"), BOT("B"), LEFT("L"), RIGHT("R");
        private String value;

        POSITION(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    public static enum Type {
        TB("TB"), BT("BT"), LR("LR"), RL("RL");
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

