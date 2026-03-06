package com.fis.fpt.service;

import com.fis.fpt.client.UaaClient;
import com.fis.fpt.domain.Form;
import com.fis.fpt.domain.Procedures;
import com.fis.fpt.domain.Version;
import com.fis.fpt.repository.FormRepository;
import com.fis.fpt.repository.ProceduresRepository;
import com.fis.fpt.repository.VersionRepository;
import com.fis.fpt.request.RequestAddProcedure;
import com.fis.fpt.security.SecurityUtils;
import com.fis.fpt.security.UserInFoDetails;
import com.fis.fpt.service.dto.CommonInfo;
import com.fis.fpt.service.dto.FormEflow;
import com.fis.fpt.service.dto.StandardResponse;
import com.fis.fpt.utils.IDGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CommonService {
    private final FormRepository formRepository;
    private final VersionRepository versionRepository;
    private final ProceduresRepository listProcedureRepository;
    private final UaaClient uaaClient;

    public CommonInfo getCommonInfo(String versionId, String formId) {
        if (formId != null) {
            Form form  = formRepository.findFormByFormId(formId);
            Version version = versionRepository.findVersionByActiveAndFormId(true, formId);
            return new CommonInfo(form, version.getVersionId());
        } else if (versionId != null) {
            Version version = versionRepository.findVersionByVersionId(versionId);
            return new CommonInfo(version);
        }

        return null;
    }

    public String getListVariable(String versionId) {
        Version version = versionRepository.findVersionByVersionId(versionId);
        CommonInfo commonInfo = new CommonInfo(version);
        return commonInfo.getVariableArr();
    }

    @Transactional
    public Procedures saveProcedure(RequestAddProcedure requestAddProcedure) {
        UserInFoDetails currentUser = SecurityUtils.getInfoCurrentUserLogin();
        Procedures listProcedure = new Procedures();
        listProcedure.setId(IDGenerator.generateIDSuffix(currentUser.getId()));
        listProcedure.setProcedureId(requestAddProcedure.getProcessId());
        listProcedure.setStepId(requestAddProcedure.getUserTaskId());
        listProcedure.setProcedureName(requestAddProcedure.getProcedureName());
        listProcedure.setStepName(requestAddProcedure.getStepName());
        Instant currentTime = Instant.now();
        listProcedure.setCreatedDate(currentTime);
        listProcedure.setCreatedBy(currentUser.getLogin().trim());
        listProcedure.setFormId(requestAddProcedure.getFormId());
        listProcedure.setOrgIn(currentUser.getOrgIn());
        listProcedure.setCustId(currentUser.getCustId());
        listProcedure.setUserId(currentUser.getId());
        Version version = versionRepository.findVersionByActiveAndFormId(true, requestAddProcedure.getFormId());
        listProcedure.setVersionId(version.getVersionId());
        if (requestAddProcedure.getUserTaskId() == null || requestAddProcedure.getUserTaskId().equals("")) {
            listProcedureRepository.deleteByProcedureIdAndStepId(requestAddProcedure.getProcessId(), null);
        } else {
            listProcedureRepository.deleteByStepId(requestAddProcedure.getUserTaskId());
        }
        listProcedureRepository.save(listProcedure);
        return listProcedure;
    }

    @Transactional
    public Procedures saveProcedureV2(RequestAddProcedure requestAddProcedure) {
        UserInFoDetails currentUser = SecurityUtils.getInfoCurrentUserLogin();
        Procedures listProcedure = new Procedures();
        listProcedure.setId(IDGenerator.generateIDSuffix(currentUser.getId()));
        listProcedure.setProcedureId(requestAddProcedure.getProcessId());
        listProcedure.setStepId(requestAddProcedure.getUserTaskId());
        listProcedure.setProcedureName(requestAddProcedure.getProcedureName());
        listProcedure.setStepName(requestAddProcedure.getStepName());
        Instant currentTime = Instant.now();
        listProcedure.setCreatedDate(currentTime);
        listProcedure.setCreatedBy(currentUser.getLogin().trim());
        listProcedure.setFormId(requestAddProcedure.getFormId());
        listProcedure.setOrgIn(currentUser.getOrgIn());
        listProcedure.setCustId(currentUser.getCustId());
        listProcedure.setUserId(currentUser.getId());
        Version version = versionRepository.findVersionByActiveAndFormId(true, requestAddProcedure.getFormId());
        listProcedure.setVersionId(version.getVersionId());
//        if(requestAddProcedure.getUserTaskId()==null){
//            listProcedureRepository.deleteByProcedureIdAndStepId(requestAddProcedure.getProcessId(), null);
//        }
//        else {
//            listProcedureRepository.deleteByStepId(requestAddProcedure.getUserTaskId());
//        }
        listProcedureRepository.save(listProcedure);
        return listProcedure;
    }


    public List<StandardResponse> getFormEflow(String procedures, String step) {
        List<StandardResponse> list = new ArrayList<>();
        UserInFoDetails currentUser = SecurityUtils.getInfoCurrentUserLogin();
        String message;
        FormEflow data;
        Instant currentTime = Instant.now();
        List<FormEflow> list1;
        if (procedures != null && !procedures.equals("")) {
            list1 = listProcedureRepository.findFormEflowByProcedure(procedures, currentUser.getLogin().trim(), currentTime, currentUser.getOrgIn());
        } else {
            list1 = listProcedureRepository.findFormEflowByStep(step, currentUser.getLogin().trim(), currentTime, currentUser.getOrgIn());
        }
        for (FormEflow formEflow : list1) {
            if (formEflow.getEffect().equals("true")) {
                message = "The form is still valid";
                data = formEflow;
                StandardResponse standardResponse = new StandardResponse(message, data);
                list.add(standardResponse);
            } else {
                message = "The form is expired";
                formEflow.setJsonForm(null);
                data = formEflow;
                StandardResponse standardResponse = new StandardResponse(message, data);
                list.add(standardResponse);
            }
        }
        return list;
    }

    @Transactional
    public void deleteProcedure(String processId, String userTaskId, String formId) {
        UserInFoDetails currentUser = SecurityUtils.getInfoCurrentUserLogin();
        if (processId != null) {
            listProcedureRepository.deleteByProcedureId(processId);
        }
        if (userTaskId != null) {
            listProcedureRepository.deleteByStepId(userTaskId);
        }
        if (formId != null) {
            listProcedureRepository.deleteByFormId(formId, currentUser.getLogin().trim(), currentUser.getOrgIn());
        }
    }

    public StandardResponse checkShare(String formId) {
        Form form = formRepository.findFormByFormId(formId);
        String message;
        if (form.getStatusForm().equals("releasing")) {
            message = "Form can share";
            return new StandardResponse(message, true);
        } else {
            message = "Form can not share";
            return new StandardResponse(message, false);
        }
    }
}
