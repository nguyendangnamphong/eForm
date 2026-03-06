package com.fis.fpt.service;


import com.fis.fpt.domain.VariableSystem;
import com.fis.fpt.domain.VariableSystemAcl;
import com.fis.fpt.domain.enums.Perm;
import com.fis.fpt.domain.enums.PermType;
import com.fis.fpt.repository.VariableSystemAclRepository;
import com.fis.fpt.repository.VariableSystemRepository;
import com.fis.fpt.security.SecurityUtils;
import com.fis.fpt.security.UserInFoDetails;
import com.fis.fpt.service.dto.StandardResponse;
import com.fis.fpt.service.dto.VariableSystemAclDTO;
import com.fis.fpt.service.dto.VariableSystemAclRequest;
import com.fis.fpt.service.dto.VariableSystemDTO;
import com.fis.fpt.utils.IDGenerator;
import com.fis.fpt.web.rest.errors.BadRequestAlertException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class VariableSystemAclService {

    private final VariableSystemRepository variableSystemRepository;
    private final VariableSystemAclRepository variableSystemAclRepository;

    public StandardResponse createVariableSystemAcl(String varId, List<VariableSystemAclRequest> variableSystemAclRequests) {
        Optional<UserInFoDetails> user = Optional.ofNullable(SecurityUtils.getInfoCurrentUserLogin());
        Optional<VariableSystem> variableSystem = variableSystemRepository.findById(varId);
        if (variableSystem.isEmpty()){
            throw new BadRequestAlertException("Variable system not found", "", null);
        }

        List<VariableSystemAclRequest> uniqueAclList = new ArrayList<>(new HashSet<>(variableSystemAclRequests));
        List<String> ignorePermList = new ArrayList<>();
        ignorePermList.add(Perm.OWNER.getValue());

        List<String> sharePerm = new ArrayList<>();
        sharePerm.add(Perm.OWNER.getValue());
        sharePerm.add(Perm.EDIT.getValue());
        if (user.isEmpty() || !checkPermShare(varId, user.get(), sharePerm))
            return new StandardResponse("You don't have permission to share", "");
        shareVariableAcl(varId, ignorePermList, uniqueAclList);
        return new StandardResponse("Share variable system acl successfully", "");
    }

    private void shareVariableAcl(String varId, List<String> ignorePermList, List<VariableSystemAclRequest> uniqueAclFormList) {
        Optional<UserInFoDetails> user = Optional.ofNullable(SecurityUtils.getInfoCurrentUserLogin());
        List<VariableSystemAclRequest> newAclForms = new ArrayList<>();
        List<VariableSystemAcl> changeAclList = new ArrayList<>();
        List<VariableSystemAcl> newAclLists = new ArrayList<>();
        Optional<VariableSystemAcl> ownerAcl = variableSystemAclRepository.findOwnerByVarId(varId, Perm.OWNER.getValue());

        List<VariableSystemAcl> aclList = variableSystemAclRepository.findByVarIdInAndIgnorePermIn(List.of(varId)
                , List.of(Perm.OWNER.getValue())
            );
        Set<Long> ownerList = variableSystemAclRepository.findByVarIdAndPermIn(varId, List.of(Perm.OWNER.getValue())).stream()
            .map(VariableSystemAcl::getAssigneeId)
            .collect(Collectors.toSet());

        uniqueAclFormList.forEach(aclForm -> {
            final boolean[] isChecked = {false};
            aclList.removeIf(acl -> {
                if (compareAcl(aclForm, acl)) {
                    isChecked[0] = true;
                    return true;
                } else if (hasChangedAcl(aclForm, acl)) {
                    if (ignorePermList.contains(aclForm.getPerm())) {
                        throw new BadRequestAlertException("Invalid change permission value", "", "");
                    }
                    acl.setPerm(aclForm.getPerm());
                    changeAclList.add(acl);
                    isChecked[0] = true;
                    return true;
                }
                return false;
            });

            ownerAcl.ifPresent(owner -> {
                if (owner.getAssignee().equals(aclForm.getAssignee()) && owner.getAssigneeId().equals(aclForm.getAssigneeId())) {
                    throw new BadRequestAlertException("Cannot add the owner to the share list", "", "");
                }
            });

            if (!isChecked[0]) {
                newAclForms.add(aclForm);
            }
        });

        newAclForms.forEach(aclForm -> {
            if (ignorePermList.contains(aclForm.getPerm())) {
                throw new BadRequestAlertException("Invalid change permission value", "", "");
            }
            if (aclForm.getPermType().equals(PermType.USER.getValue()) && ownerList.contains(aclForm.getAssigneeId())) {
                return;
            }
            VariableSystemAcl initAcl = initNewAclFromAclForm(aclForm, varId, user);
            newAclLists.add(initAcl);
        });
        variableSystemAclRepository.saveAll(changeAclList);
        variableSystemAclRepository.saveAll(newAclLists);
    }

    private VariableSystemAcl initNewAclFromAclForm(VariableSystemAclRequest aclForm, String varId, Optional<UserInFoDetails> user) {
        VariableSystemAcl variableSystemAcl = new VariableSystemAcl();
        variableSystemAcl.setId(IDGenerator.generateIDSuffix(user.get().getId()));
        variableSystemAcl.setVarId(varId);
        variableSystemAcl.setPerm(aclForm.getPerm());
        variableSystemAcl.setPermType(PermType.findKey(aclForm.getPermType()).getValue());
        variableSystemAcl.setAssigneeId(aclForm.getAssigneeId());
        variableSystemAcl.setAssignee(aclForm.getAssignee());
        variableSystemAcl.setAuthorizerId(user.get().getId());
        variableSystemAcl.setAuthorizer(user.get().getLogin());
        variableSystemAcl.setUserId(user.get().getId());
        variableSystemAcl.setOrgIn(user.get().getOrgIn());
        variableSystemAcl.setCustId(user.get().getCustId());
        variableSystemAcl.setCreatedBy(user.get().getEmail());
        variableSystemAcl.setLastModifiedBy(user.get().getEmail());
        variableSystemAcl.setLastModifiedDate(Instant.now());
        variableSystemAcl.setCreatedDate(Instant.now());

        return variableSystemAcl;
    }


    private boolean compareAcl(VariableSystemAclRequest aclForm, VariableSystemAcl acl) {
        return Objects.equals(aclForm.getPerm(), acl.getPerm())
            && Objects.equals(aclForm.getPermType(), acl.getPermType())
            && Objects.equals(aclForm.getAssignee(), acl.getAssignee())
            && Objects.equals(aclForm.getAssigneeId(), acl.getAssigneeId());
    }

    private boolean hasChangedAcl(VariableSystemAclRequest aclForm, VariableSystemAcl acl) {
        return !Objects.equals(aclForm.getPerm(), acl.getPerm())
            && Objects.equals(aclForm.getPermType(), acl.getPermType())
            && Objects.equals(aclForm.getAssignee(), acl.getAssignee())
            && Objects.equals(aclForm.getAssigneeId(), acl.getAssigneeId());
    }

    private boolean checkPermShare(String varId, UserInFoDetails user, List<String> permList) {
        List<Long> orgIds = getOrgIds(user);

        Set<VariableSystemAcl> shareList = new HashSet<>(variableSystemAclRepository.findByVarIdAndPermIn(varId, permList));

        return shareList.stream().anyMatch(
            info ->
                (info.getAssigneeId().equals(user.getId()) && info.getPermType().equals(PermType.USER.getValue())) ||
                    (orgIds.contains(info.getAssigneeId()) && info.getPermType().equals(PermType.ORG.getValue()))
        );
    }

    protected List<Long> getOrgIds(UserInFoDetails user) {
        return Arrays.stream(user.getOrgIn().split("/"))
            .skip(1)
            .map(Long::parseLong)
            .collect(Collectors.toList());
    }

    public StandardResponse updateVariableSystemAcl(List<VariableSystemAclDTO> variableSystemAcls) {
        Optional<UserInFoDetails> user = Optional.ofNullable(SecurityUtils.getInfoCurrentUserLogin());
        List<String> sharePerm = new ArrayList<>();
        sharePerm.add(Perm.OWNER.getValue());
        sharePerm.add(Perm.EDIT.getValue());

        List<VariableSystemAcl> result = new ArrayList<>();
        List<VariableSystemAclDTO> uniqueAcl = new ArrayList<>(new HashSet<>(variableSystemAcls));
        List<String> aclIds = uniqueAcl.stream()
            .map(VariableSystemAclDTO::getId)
            .collect(Collectors.toList());
        Map<String, VariableSystemAclDTO> map = uniqueAcl.stream()
            .collect(Collectors.toMap(VariableSystemAclDTO::getId, acl -> acl));

        List<VariableSystemAcl> aclList = variableSystemAclRepository.findAllByIdIn((aclIds)).stream()
            .filter(t -> !Objects.equals(t.getPerm(), Perm.OWNER.getValue()))
            .collect(Collectors.toList());

        for (VariableSystemAcl acl : aclList) {
            if (user.isEmpty() || !checkPermShare(acl.getVarId(), user.get(), sharePerm))
                throw new BadRequestAlertException("No access permission", "", "");
            VariableSystemAclDTO aclDTO = map.get(acl.getId());
            updateAcl(aclDTO, acl);

        }
        result = variableSystemAclRepository.saveAll(aclList);
        return new StandardResponse("Success", result);
    }

    private void updateAcl(VariableSystemAclDTO aclDTO, VariableSystemAcl aclEntity) {
        aclEntity.setVarId(aclDTO.getVarId());
        aclEntity.setPerm(aclDTO.getPerm());
        aclEntity.setPermType(aclDTO.getPermType());
        aclEntity.setAssignee(aclDTO.getAssignee());
        aclEntity.setAssigneeId(aclDTO.getAssigneeId());
        aclEntity.setAuthorizer(aclDTO.getAuthorizer());
        aclEntity.setAuthorizerId(aclDTO.getAuthorizerId());
        aclEntity.setUserId(aclDTO.getUserId());
        aclEntity.setOrgIn(aclDTO.getOrgIn());

    }

    public StandardResponse getAllVariableSystemAcls(String varId) {
        Optional<UserInFoDetails> user = Optional.ofNullable(SecurityUtils.getInfoCurrentUserLogin());
        List<VariableSystemAcl> aclList1 = variableSystemAclRepository.findAllByVarId((varId));
        List<VariableSystemAcl> result = new ArrayList<>();

        result.addAll(aclList1.stream().filter(t -> t.getAssigneeId() != null)
            .filter(t -> Objects.equals(t.getPerm(), Perm.OWNER.getValue()))
            .collect(Collectors.toList()));

        result.addAll(aclList1.stream()
            .filter(t -> t.getAssigneeId() != null)
            .filter(t -> !Objects.equals(t.getPerm(), Perm.OWNER.getValue()))
            .collect(Collectors.toList()));

        return new StandardResponse("Success", result);
    }

    public StandardResponse deleteVariableSystemAclList(List<String> ids) {
        Optional<UserInFoDetails> user = Optional.ofNullable(SecurityUtils.getInfoCurrentUserLogin());
        List<String> sharePerm = new ArrayList<>();
        sharePerm.add(Perm.OWNER.getValue());
        sharePerm.add(Perm.EDIT.getValue());

        List<VariableSystemAcl> aclList = variableSystemAclRepository.findAllByIdIn(ids).stream()
            .filter(t -> !Objects.equals(t.getPerm(), Perm.OWNER.getValue()))
            .collect(Collectors.toList());

        for (VariableSystemAcl acl : aclList) {
            if (user.isEmpty() || !checkPermShare(acl.getVarId(), user.get(), sharePerm))
                throw new BadRequestAlertException("No access permission", "", "");
        }
        variableSystemAclRepository.deleteAll(aclList);
        return new StandardResponse("Delete variable system acl successfully", "");
    }

    public StandardResponse deleteVariableSystemAcl(String id) {
        log.debug("Request to delete VariableSystemAcl: {}", id);
        UserInFoDetails user = SecurityUtils.getInfoCurrentUserLogin();
        List<String> sharePerms = List.of(Perm.OWNER.getValue(), Perm.EDIT.getValue());

        VariableSystemAcl acl = variableSystemAclRepository.findById(id);

        if (acl == null) {
            throw new BadRequestAlertException("Variable system acl not found", "", "");
        }

        if (Objects.equals(acl.getPerm(), Perm.OWNER.getValue())) {
            throw new BadRequestAlertException("Cannot delete OWNER permission", "", "");
        }

        if (!checkPermShare(acl.getVarId(), user, sharePerms)) {
            throw new BadRequestAlertException("No access permission", "", "");
        }

        variableSystemAclRepository.delete(acl);
        return new StandardResponse("Success", "Delete variable system acl successfully");
    }

}
