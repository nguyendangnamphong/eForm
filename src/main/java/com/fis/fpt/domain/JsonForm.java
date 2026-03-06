package com.fis.fpt.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class JsonForm implements Serializable {
    public enum formTypes {
        container, row, columns, step, tab, divider, text, h1, h2, h3, bulleted_list, numbered_list,
        textField, textArea, number, password, email, phone_number, radio_button, check_box,
        dropdown, datepicker, fieldSet, table, upload_file, upload_image, hyperlink,
        step_child, tab_child, splitter
    }

    public enum formInputTypes {
        text, number, email, password
    }

    public enum labelPositionValue {
        left, above
    }

    public enum textPositionValue {
        left, right, center
    }

    public enum textTransformValue {
        uppercase, lowercase, none
    }

    public formTypes type = null;
    public String label = null;
    public String placeholder = null;
    public formInputTypes inputType = null;


    public String value = null;
    public String tableConfig = null;
    public String radioConfig = null;
    public String dropdownConfig = null;
    public String checkBoxConfig = null;


    public List<JsonForm> components = null;
    public String id = null;
    public labelPositionValue labelPosition = null;
    public textPositionValue textPosition = null;
    public String tooltip = null;
    public String defaultValue = null;
    public textTransformValue textTransform = null;
    public formValidate validate = null;
    public Integer mutilRows = null;
    public String description = null;
    public Boolean display = null;
    public Boolean readonly = null;
    public Boolean useDateLocal = null;

    public List<String> bulletedList = null;
    public List<String> numberedList = null;

}
