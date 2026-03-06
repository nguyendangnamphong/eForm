package com.fis.fpt.uaadomain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FieldTextDesign implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3491280577330756566L;

    private String position = FieldDesign.POSITION.RIGHT.toString();

    private Boolean required = true;

    private List<TextLabel> items = new ArrayList<>();

    private String color;

    private String font;

    private TextDisplay textDisplay;

    private Boolean multiLine = false;

    public Boolean getMultiLine() {
        return multiLine;
    }

    public void setMultiLine(Boolean multiLine) {
        this.multiLine = multiLine;
    }

    public TextDisplay getTextDisplay() {
        return textDisplay;
    }

    public void setTextDisplay(TextDisplay textDisplay) {
        this.textDisplay = textDisplay;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String findMaxItems() {
        if (items != null && items.size() > 0) {

            String maxValue = null;
            TextLabel max = items.get(0);
            for (TextLabel item : items) {
                if (item.getValue() != null && item.getValue().length() > max.getValue().length()) {
                    maxValue = item.getValue();
                }
            }
            return maxValue;
        }
        return null;
    }

    public List<TextLabel> getItems() {
        return items;
    }

    public void setItems(List<TextLabel> items) {
        this.items = items;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

}
