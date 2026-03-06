package com.fis.fpt.service;

import com.fis.fpt.client.UaaClient;
import com.fis.fpt.converter.DateConverter;
import com.fis.fpt.domain.Acl;
import com.fis.fpt.domain.Form;
import com.fis.fpt.domain.Version;
import com.fis.fpt.domain.enums.StatusForm;
import com.fis.fpt.repository.AclRepository;
import com.fis.fpt.repository.FormRepository;
import com.fis.fpt.repository.ProceduresRepository;
import com.fis.fpt.repository.VersionRepository;
import com.fis.fpt.request.FormSearchEflow;
import com.fis.fpt.request.RequestAddForm;
import com.fis.fpt.search.FormSearchShare;
import com.fis.fpt.search.FormSearchShareDto;
import com.fis.fpt.security.SecurityUtils;
import com.fis.fpt.security.UserInFoDetails;
import com.fis.fpt.service.dto.*;
import com.fis.fpt.uaadomain.OrganizationDTO;
import com.fis.fpt.uaadomain.UserDTO;
import com.fis.fpt.utils.IDGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShareService {
    private final AclRepository aclRepository;
    private final FormRepository formRepository;
    private final VersionRepository versionRepository;
    private final ProceduresRepository listProcedureRepository;
    private final UaaClient uaaClient;

    public Page<FormShareDto> getFormShare(FormSearchShareDto formSearchShareDto, Pageable pageable) {
        FormSearchShare formSearchShare = new FormSearchShare(formSearchShareDto);
        UserInFoDetails currentUser = SecurityUtils.getInfoCurrentUserLogin();
        String orgIn = currentUser.getOrgIn();
        formSearchShare.setUsername(currentUser.getLogin().trim());
        Page<FormShareDto> formShareDtos;
        if (formSearchShare.getCreatedByList() == null || formSearchShare.getCreatedByList().size() == 0) {
            formShareDtos = formRepository.findAllFormShare(formSearchShare, orgIn, pageable);
        } else {
            formShareDtos = formRepository.findFormShare(formSearchShare, orgIn, pageable);
        }
        for (FormShareDto form : formShareDtos) {
            UserDTO userDTO = uaaClient.getInfo(form.getCreatedBy());
            if (userDTO.getAvatar() != null) {
                form.setAvatar(Base64.getEncoder().encodeToString(userDTO.getAvatar()));
            }
            form.setFullName(userDTO.getFirstName() + " " + userDTO.getLastName());
            String formId = form.getFormId();
            List<String> procedures = listProcedureRepository.getProcedure(formId);
            String procedure = "";
            for (String str : procedures) {
                procedure = procedure + str + ", ";
            }
            if (procedure.length() >= 2) {
                String modifiedString = procedure.substring(0, procedure.length() - 2);
                form.setListProcedure(modifiedString);
            } else {
                form.setListProcedure(procedure);
            }
        }
        return formShareDtos;
    }



    public Form duplicateFormShare(String formId, RequestAddForm requestAddForm) {
        UserInFoDetails currentUser = SecurityUtils.getInfoCurrentUserLogin();
        Form form1 = formRepository.findFormByFormId(formId);
        Form form = new Form();
        form.setFormId(IDGenerator.generateIDSuffix(currentUser.getId()));
        form.setFormCode(requestAddForm.getFormCode());
        form.setFormName(requestAddForm.getFormName());
        form.setTag(requestAddForm.getTag());
        form.setBeginTime(DateConverter.parseStringToZonedDateTime(requestAddForm.getBeginTime()));
        form.setEndTime(DateConverter.parseStringToZonedDateTime3(requestAddForm.getEndTime()));
        form.setDescription(requestAddForm.getDescribeForm());
        form.setJsonForm(form1.getJsonForm());
        form.setJsonFormCondition(form1.getJsonFormCondition());
        form.setCodeJson(form1.getCodeJson());
        form.setConfigWriter(form1.getConfigWriter());

        form.setCreatedBy(currentUser.getLogin().trim());
        Instant currentTime = Instant.now();
        form.setCreatedDate(currentTime);
        form.setLastModifiedDate(currentTime);
        form.setStatusForm(StatusForm.DRAFT.getValue());
        form.setOrgIn(currentUser.getOrgIn());
        form.setUserId(currentUser.getId());
        form.setCustId(currentUser.getCustId());
        form.setLastModifiedBy(currentUser.getLogin().trim());
        form.setVariableArr(form1.getVariableArr());
        Form form2 = formRepository.save(form);


        Acl authorize = new Acl();
        authorize.setIdAcl(IDGenerator.generateIDSuffix(currentUser.getId()));
        authorize.setFormId(form2.getFormId());
        authorize.setCreatedBy(currentUser.getLogin().trim());
        authorize.setRole(1L);
        authorize.setOrgIn(currentUser.getOrgIn());
        authorize.setLastModifiedDate(currentTime);
        authorize.setUserId(currentUser.getId());
        authorize.setEmail(currentUser.getLogin().trim());
        authorize.setStatus(1L);
        authorize.setCustId(currentUser.getCustId());
        authorize.setCreatedDate(currentTime);
        authorize.setLastModifiedBy(currentUser.getLogin().trim());
        aclRepository.save(authorize);

        return form;
    }

    public Page<FormShareDto> getFormEflow(FormSearchEflow formSearchEflow, Pageable pageable) {
        Instant createdDate1 = null;
        Instant createdDate2 = null;
        Instant currentTime = Instant.now();
        if (formSearchEflow.getCreatedDate() != null && !formSearchEflow.getCreatedDate().isEmpty()) {
            ZonedDateTime zonedDateTime = DateConverter.parseStringToZonedDateTime2(formSearchEflow.getCreatedDate());
            createdDate1 = zonedDateTime.with(LocalTime.MIN).toInstant();
            createdDate2 = zonedDateTime.with(LocalTime.MAX).toInstant();
        }
        UserInFoDetails currentUser = SecurityUtils.getInfoCurrentUserLogin();
        Page<FormShareDto> formShareDtos = formRepository.findFormEflow(currentTime, createdDate1, createdDate2, formSearchEflow.getTextSearch(), currentUser.getLogin().trim(), currentUser.getOrgIn(), pageable);
        for (FormShareDto form : formShareDtos) {
            form.setFullName("");
            String formId = form.getFormId();
            List<String> procedures = listProcedureRepository.getProcedure(formId);
            String procedure = "";
            for (String str : procedures) {
                procedure = procedure + str + ", ";
            }
            if (procedure.length() >= 2) {
                String modifiedString = procedure.substring(0, procedure.length() - 2);
                form.setListProcedure(modifiedString);
            } else {
                form.setListProcedure(procedure);
            }
        }
        return formShareDtos;
    }

    public List<Version> getListVersion(List<String> versionIds) {
        List<Version> versions = new ArrayList<>();
        for(String versionId:versionIds){
            versions.add(versionRepository.findVersionByVersionId(versionId));
        }
        return versions;
    }

    public OrginShare getInfoShare(String formId, String search) {
        OrginShare orginShare = new OrginShare();
        List<AuthorizeDto> list = aclRepository.getInfoShare(formId);
        List<InfoOrginShare> list1 = new ArrayList<>();
        UserInFoDetails currentUser = SecurityUtils.getInfoCurrentUserLogin();
        String orgIn = currentUser.getOrgIn();
        String[] listOrg = orgIn.split("/");
        String orgInParam = "/" + listOrg[1] + "/" + listOrg[2];
        List<OrganizationDTO> organizationDTOList = uaaClient.findOrigin(orgInParam);
        Long cnt = 0L;
        for (AuthorizeDto authorizeDto : list) {
            InfoOrginShare infoOrginShare = new InfoOrginShare();
            for (OrganizationDTO organizationDTO : organizationDTOList) {
                if (organizationDTO.getOrgIn().equals(authorizeDto.orgIn)) {
                    infoOrginShare.orgName = organizationDTO.getName();
                    break;
                }
            }
            String[] emails = authorizeDto.arrEmail.split(",");
            cnt += emails.length;
            List<AccountInfo> accountInfos = new ArrayList<>();
            for (String email : emails) {

                UserDTO userDTO = uaaClient.getInfo(email);
                AccountInfo accountInfo = new AccountInfo();
                accountInfo.setAvatar(userDTO.getAvatar() == null ? null : Base64.getEncoder().encodeToString(userDTO.getAvatar()));
                accountInfo.fullName = userDTO.getFirstName() + " " + userDTO.getLastName();
                accountInfo.email = email;
                accountInfo.orgIn = authorizeDto.getOrgIn();
                accountInfos.add(accountInfo);
                infoOrginShare.listMember = accountInfos;
            }
            list1.add(infoOrginShare);
        }
        orginShare.numberMember = cnt;
        orginShare.data = list1;

        OrginShare orginShare1 = new OrginShare();
        orginShare1.numberMember = orginShare.numberMember;
        for (InfoOrginShare org : orginShare.data) {
            if (org.orgName.toLowerCase().contains(search.toLowerCase())) {
                orginShare1.data.add(org);
            } else {
                InfoOrginShare org1 = new InfoOrginShare();
                org1.orgName = org.orgName;
                for (AccountInfo member : org.listMember) {
                    if (member.email.toLowerCase().contains(search.toLowerCase())) {
                        org1.listMember.add(member);
                    }
                }
                if (org1.listMember.size() != 0) {
                    orginShare1.data.add(org1);
                }
            }
        }

        return orginShare1;
    }

}
