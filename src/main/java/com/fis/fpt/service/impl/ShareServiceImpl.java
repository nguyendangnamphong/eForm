//package com.fis.fpt.service.impl;
//
//import com.fis.fpt.converter.DateConverter;
//import com.fis.fpt.request.FormSearchEflow;
//import com.fis.fpt.request.RequestAddForm;
//import com.fis.fpt.search.FormSearchShare;
//import com.fis.fpt.search.FormSearchShareDto;
//import com.fis.fpt.uaadomain.OrganizationDTO;
//import com.fis.fpt.uaadomain.PersonalKycDetailProfileDTO;
//import com.fis.fpt.uaadomain.UserDTO;
//import com.fis.fpt.client.UaaClient;
//import com.fis.fpt.domain.Form;
//import com.fis.fpt.repository.FormRepository;
//import com.fis.fpt.repository.ListProcedureRepository;
//import com.fis.fpt.repository.VersionRepository;
//import com.fis.fpt.security.SecurityUtils;
//import com.fis.fpt.security.UserInFoDetails;
//import com.fis.fpt.service.dto.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//import java.util.ArrayList;
//import java.util.Base64;
//import java.util.List;
//
//@Service
//public class ShareServiceImpl {
//    @Autowired
//    private final UaaClient uaaClient;
//    @Autowired
//    private final AuthorizeRepository authorizeRepository;
//    @Autowired
//    private final FormRepository formRepository;
//    @Autowired
//    private final VersionRepository versionRepository;
//    @Autowired
//    private final ListProcedureRepository listProcedureRepository;
//
//    public ShareServiceImpl(UaaClient uaaClient, AuthorizeRepository authorizeRepository, FormRepository formRepository, VersionRepository versionRepository, ListProcedureRepository listProcedureRepository) {
//        this.uaaClient = uaaClient;
//        this.authorizeRepository = authorizeRepository;
//        this.formRepository = formRepository;
//        this.versionRepository = versionRepository;
//        this.listProcedureRepository = listProcedureRepository;
//    }
//
//
//    public Page<FormShareDto> getFormShare(FormSearchShareDto formSearchShareDto, Pageable pageable) {
//        FormSearchShare formSearchShare = new FormSearchShare(formSearchShareDto);
//        UserInFoDetails currentUser = SecurityUtils.getInfoCurrentUserLogin();
//        String orgIn = currentUser.getOrgIn();
//        formSearchShare.setUsername(currentUser.getLogin().trim());
//        Page<FormShareDto> formShareDtos;
//        if(formSearchShare.getCreatedByList()==null||formSearchShare.getCreatedByList().size()==0){
//            formShareDtos = formRepository.findAllFormShare(formSearchShare, orgIn, pageable);
//        }
//        else {
//            formShareDtos = formRepository.findFormShare(formSearchShare, orgIn, pageable);
//        }
//        for (FormShareDto form : formShareDtos) {
//            UserDTO userDTO = uaaClient.getInfo(form.getCreatedBy());
//            if(userDTO.getAvatar()!=null) {
//                form.setAvatar(Base64.getEncoder().encodeToString(userDTO.getAvatar()));
//            }
//            form.setFullName(userDTO.getFirstName()+" "+userDTO.getLastName());
//            String formId = form.getFormId();
//            List<String> procedures = listProcedureRepository.getProcedure(formId);
//            String procedure = "";
//            for (String str : procedures) {
//                procedure = procedure + str + ", ";
//            }
//            if (procedure.length() >= 2) {
//                String modifiedString = procedure.substring(0, procedure.length() - 2);
//                form.setListProcedure(modifiedString);
//            } else {
//                form.setListProcedure(procedure);
//            }
//        }
//        return formShareDtos;
//    }
//
//
//    public Page<FormShareDto> getFormEflow(FormSearchEflow formSearchEflow, Pageable pageable) {
//        ZonedDateTime createdDate1 = null;
//        ZonedDateTime createdDate2 = null;
//        ZoneId zoneId = ZoneId.of("UTC");
//        ZonedDateTime currentTime = ZonedDateTime.now(zoneId);
//        if(formSearchEflow.getCreatedDate()!=null) {
//            createdDate1 = DateConverter.parseStringToZonedDateTime(formSearchEflow.getCreatedDate());
//            createdDate2 = DateConverter.parseStringToZonedDateTime3(formSearchEflow.getCreatedDate());
//        }
//        UserInFoDetails currentUser = SecurityUtils.getInfoCurrentUserLogin();
//        Page<FormShareDto> formShareDtos = formRepository.findFormEflow(currentTime, createdDate1, createdDate2, formSearchEflow.getTextSearch(), currentUser.getLogin().trim(), currentUser.getOrgIn(), pageable);
//        for (FormShareDto form : formShareDtos) {
//            UserDTO userDTO = uaaClient.getInfo(form.getCreatedBy());
//            try {
//            PersonalKycDetailProfileDTO personalKycDetailProfileDTO = uaaClient.getInfoId(userDTO.getId());
//            form.setFullName(personalKycDetailProfileDTO.getPersonalName());
//            }
//            catch (Exception e){
//                if(userDTO.getFirstName().equals("")&&userDTO.getLastName().equals("")) {
//                    form.setFullName("");
//                }
//                else  form.setFullName(userDTO.getFirstName()+" "+userDTO.getLastName());
//            }
//            if(userDTO.getAvatar()!=null) {
//                form.setAvatar(Base64.getEncoder().encodeToString(userDTO.getAvatar()));
//            }
//            String formId = form.getFormId();
//            List<String> procedures = listProcedureRepository.getProcedure(formId);
//            String procedure = "";
//            for (String str : procedures) {
//                procedure = procedure + str + ", ";
//            }
//            if (procedure.length() >= 2) {
//                String modifiedString = procedure.substring(0, procedure.length() - 2);
//                form.setListProcedure(modifiedString);
//            } else {
//                form.setListProcedure(procedure);
//            }
//        }
//        return formShareDtos;
//    }
//
//    public Form duplicateFormShare(String formId, RequestAddForm requestAddForm) {
//        Form form1 = formRepository.findFormByFormId(formId);
//        Form form = new Form();
//        form.setFormCode(requestAddForm.getFormCode());
//        form.setFormName(requestAddForm.getFormName());
//        form.setTag(requestAddForm.getTag());
//        form.setBeginTime(DateConverter.parseStringToZonedDateTime(requestAddForm.getBeginTime()));
//        form.setEndTime(DateConverter.parseStringToZonedDateTime3(requestAddForm.getEndTime()));
//        form.setDescribeForm(requestAddForm.getDescribeForm());
//        form.setJsonForm(form1.getJsonForm());
//        UserInFoDetails currentUser = SecurityUtils.getInfoCurrentUserLogin();
//        form.setCreatedBy(currentUser.getLogin().trim());
//        ZoneId zoneId = ZoneId.of("UTC");
//        ZonedDateTime currentTime = ZonedDateTime.now(zoneId);
//        form.setCreatedDate(currentTime);
//        form.setUpdateAt(currentTime);
//        form.setStatusForm("draft");
//        form.setOrgIn(currentUser.getOrgIn());
//        formRepository.save(form);
//
//        Authorize authorize = new Authorize();
//        FormDto formDto = formRepository.findFormByCreatedDate(currentUser.getLogin().trim(), currentUser.getOrgIn(), currentTime);
//        authorize.setFormId(formDto.getFormId());
//        authorize.setUsername(currentUser.getLogin().trim());
//        authorize.setRoles("owner");
//        authorize.setOrgIn(currentUser.getOrgIn());
//        authorize.setCreatedAt(currentTime);
//        authorizeRepository.save(authorize);
//
//        return form;
//    }
//
//    public OrginShare getInfoShare(String formId, String search) {
//        OrginShare orginShare = new OrginShare();
//        List<AuthorizeDto> list = authorizeRepository.getInfoShare(formId);
//        List<InfoOrginShare> list1 = new ArrayList<>();
//        UserInFoDetails currentUser = SecurityUtils.getInfoCurrentUserLogin();
//        String orgIn = currentUser.getOrgIn();
//        String[] listOrg = orgIn.split("/");
//        String orgInParam = "/"+listOrg[1]+"/"+listOrg[2];
//        List<OrganizationDTO> organizationDTOList = uaaClient.findOrigin(orgInParam);
//        Long cnt = 0L;
//        for (AuthorizeDto authorizeDto : list) {
//            InfoOrginShare infoOrginShare = new InfoOrginShare();
//            for (OrganizationDTO organizationDTO : organizationDTOList) {
//                if (organizationDTO.getOrgIn().equals(authorizeDto.orgIn)) {
//                    infoOrginShare.orgName = organizationDTO.getName();
//                    break;
//                }
//            }
//            String[] emails = authorizeDto.arrEmail.split(",");
//            cnt += emails.length;
//            List<AccountInfo> accountInfos = new ArrayList<>();
//            for (String email : emails) {
//
//                UserDTO userDTO = uaaClient.getInfo(email);
//                AccountInfo accountInfo = new AccountInfo();
//                accountInfo.setAvatar(userDTO.getAvatar() == null ? null : Base64.getEncoder().encodeToString(userDTO.getAvatar()));
//                accountInfo.fullName = userDTO.getFirstName() + " " + userDTO.getLastName();
//                accountInfo.email = email;
//                accountInfo.orgIn = userDTO.getOrgIn();
//                accountInfos.add(accountInfo);
//                infoOrginShare.listMember = accountInfos;
//            }
//            list1.add(infoOrginShare);
//        }
//        orginShare.numberMember = cnt;
//        orginShare.data = list1;
//
//        OrginShare orginShare1 = new OrginShare();
//        orginShare1.numberMember = orginShare.numberMember;
//        for (InfoOrginShare org : orginShare.data) {
//            if (org.orgName.toLowerCase().contains(search.toLowerCase())) {
//                orginShare1.data.add(org);
//            } else {
//                InfoOrginShare org1 = new InfoOrginShare();
//                org1.orgName = org.orgName;
//                for (AccountInfo member : org.listMember) {
//                    if (member.email.toLowerCase().contains(search.toLowerCase())) {
//                        org1.listMember.add(member);
//                    }
//                }
//                if (org1.listMember.size() != 0) {
//                    orginShare1.data.add(org1);
//                }
//            }
//        }
//
//        return orginShare1;
//    }
//
//
//    public OrginShare getInfoSharePage(String formId, String search, Pageable pageable) {
//        OrginShare orginShare = new OrginShare();
//        Page<AuthorizeDto> list = authorizeRepository.getInfoSharePage(formId, pageable);
//        List<InfoOrginShare> list1 = new ArrayList<>();
//
//        UserInFoDetails currentUser = SecurityUtils.getInfoCurrentUserLogin();
//        String orgIn = currentUser.getOrgIn();
//        String[] listOrg = orgIn.split("/");
//        List<OrganizationDTO> organizationDTOList = uaaClient.findOrigin("/"+listOrg[0]+"/"+listOrg[1]);
//
//
//        Long cnt = 0L;
//        for (AuthorizeDto authorizeDto : list) {
//            InfoOrginShare infoOrginShare = new InfoOrginShare();
//            for (OrganizationDTO organizationDTO : organizationDTOList) {
//                if (organizationDTO.getOrgIn().equals(authorizeDto.orgIn)) {
//                    infoOrginShare.orgName = organizationDTO.getName();
//                    break;
//                }
//            }
//            String[] emails = authorizeDto.arrEmail.split(",");
//            cnt += emails.length;
//            List<AccountInfo> accountInfos = new ArrayList<>();
//            for (String email : emails) {
//                UserDTO userDTO = uaaClient.getInfo(email);
//                AccountInfo accountInfo = new AccountInfo();
//                if(userDTO.getAvatar()!=null) {
//                    accountInfo.setAvatar(Base64.getEncoder().encodeToString(userDTO.getAvatar()));
//                }
//                accountInfo.fullName = userDTO.getFirstName() + " " + userDTO.getLastName();
//                accountInfo.email = email;
//                accountInfo.orgIn = userDTO.getOrgIn();
//                accountInfos.add(accountInfo);
//                infoOrginShare.listMember = accountInfos;
//            }
//            list1.add(infoOrginShare);
//        }
//        orginShare.numberMember = cnt;
//        orginShare.data = list1;
//
//        OrginShare orginShare1 = new OrginShare();
//        orginShare1.numberMember = orginShare.numberMember;
//        for (InfoOrginShare org : orginShare.data) {
//            if (org.orgName.toLowerCase().contains(search.toLowerCase())) {
//                orginShare1.data.add(org);
//            } else {
//                InfoOrginShare org1 = new InfoOrginShare();
//                org1.orgName = org.orgName;
//                for (AccountInfo member : org.listMember) {
//                    if (member.email.toLowerCase().contains(search.toLowerCase())) {
//                        org1.listMember.add(member);
//                    }
//                }
//                if (org1.listMember.size() != 0) {
//                    orginShare1.data.add(org1);
//                }
//            }
//        }
//
//        return orginShare1;
//    }
//
//
//    public String[] getInfoShareOrgIn(String formId, String orgIn) {
//        AuthorizeDto authorizeDto = authorizeRepository.getInfoShareOrgIn(formId, orgIn);
//        String[] email = authorizeDto.arrEmail.split(",");
//        return email;
//    }
//
//}
