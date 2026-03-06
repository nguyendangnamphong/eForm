package com.fis.fpt.request;

import lombok.Data;

@Data
public class RequestAddProcedure {
    private String formId;
    private String processId;
    private String userTaskId;
    private String procedureName;
    private String stepName;
    private String versionId;
}
