package com.fis.fpt.request;

import lombok.Data;

@Data
public class RequestUpdateProcedure {
    private String processId;
    private String userTaskId;
    private String processName;
    private String userTaskName;
}
