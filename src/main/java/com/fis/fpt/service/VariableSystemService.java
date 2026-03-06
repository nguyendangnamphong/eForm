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
import com.fis.fpt.service.dto.VariableSystemUpdateRequest;
import com.fis.fpt.utils.IDGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class VariableSystemService {

    private final VariableSystemRepository variableSystemRepository;
    private final VariableSystemAclRepository variableSystemAclRepository;
    private final EntityManager entityManager;

    private UserInFoDetails getCurrentUser() {
        return Optional.ofNullable(SecurityUtils.getInfoCurrentUserLogin())
            .orElseThrow(() -> new RuntimeException("User not authenticated"));
    }

    public StandardResponse createVariableSystem(String variableSystemRequest) {
        log.debug("Request to save VariableSystem : {}", variableSystemRequest);
        UserInFoDetails currentUser = getCurrentUser();

        if (variableSystemRequest.isEmpty()){
            throw new RuntimeException("Variable system content cannot be empty");
        }

        VariableSystem variableSystem = new VariableSystem();
        variableSystem.setId(IDGenerator.generateIDSuffix(currentUser.getId()));
        variableSystem.setVariableSystem(variableSystemRequest);
        variableSystem.setCreatedBy(currentUser.getEmail());
        variableSystem.setLastModifiedBy(currentUser.getEmail());
        variableSystem.setUserId(currentUser.getId());
        variableSystem.setCustId(currentUser.getCustId());
        variableSystem.setOrgIn(currentUser.getOrgIn());
        variableSystem.setCreatedDate(Instant.now());
        variableSystem.setLastModifiedDate(Instant.now());
        variableSystem.setLastModifiedBy(currentUser.getEmail());
        variableSystemRepository.save(variableSystem);

        VariableSystemAcl variableSystemAcl = new VariableSystemAcl();
        variableSystemAcl.setId(IDGenerator.generateIDSuffix(currentUser.getId()));
        variableSystemAcl.setVarId(variableSystem.getId());
        variableSystemAcl.setAssignee(currentUser.getLogin());
        variableSystemAcl.setAssigneeId(currentUser.getId());
        variableSystemAcl.setAuthorizer(currentUser.getLogin());
        variableSystemAcl.setAuthorizerId(currentUser.getId());
        variableSystemAcl.setPerm(Perm.OWNER.getValue());
        variableSystemAcl.setPermType(PermType.USER.getValue());
        variableSystemAcl.setUserId(currentUser.getId());
        variableSystemAcl.setCustId(currentUser.getCustId());
        variableSystemAcl.setOrgIn(currentUser.getOrgIn());
        variableSystemAcl.setCreatedBy(currentUser.getEmail());
        variableSystemAcl.setLastModifiedBy(currentUser.getEmail());
        variableSystemAcl.setCreatedDate(Instant.now());
        variableSystemAcl.setLastModifiedDate(Instant.now());
        variableSystemAclRepository.save(variableSystemAcl);

        return new StandardResponse("Success", "Create variable system successfully");
    }

    public StandardResponse getAllVariableSystems() {
        log.debug("Request to get all VariableSystems");
        UserInFoDetails currentUser = getCurrentUser();
        List<VariableSystem> variableSystems = variableSystemRepository.findAllByUserId(currentUser.getId());
        return new StandardResponse("Success", variableSystems);
    }

    public StandardResponse getVariableSystemsShare() {
        UserInFoDetails currentUser = getCurrentUser();
        String[] orgIn = currentUser.getOrgIn().split("/");
        orgIn = Arrays.copyOfRange(orgIn, 1, orgIn.length);
        List<Long> orgIds = Arrays.stream(orgIn)
            .map(Long::parseLong)
            .collect(Collectors.toList());
        List<VariableSystem> variableSystems;
        variableSystems = findAllByVariableSystemShare(
            currentUser.getId(),
            orgIds,
            PermType.USER.getValue(),
            PermType.ORG.getValue(),
            Perm.OWNER.getValue()
        );

        return new StandardResponse("Success", variableSystems);

    }

    public StandardResponse updateVariableSystem(VariableSystemUpdateRequest variableSystemUpdateRequest) {
        log.debug("Request to update VariableSystem : {}", variableSystemUpdateRequest);
        UserInFoDetails currentUser = getCurrentUser();

        VariableSystem variableSystem = variableSystemRepository.findById(variableSystemUpdateRequest.getId())
            .orElseThrow(() -> new RuntimeException("VariableSystem not found with id " + variableSystemUpdateRequest.getId()));

        List<VariableSystemAcl> variableSystemAcls = variableSystemAclRepository.findAllByVarIdAndAssigneeId(variableSystem.getId(), currentUser.getId());
        if (variableSystemAcls.stream().noneMatch(acl -> Perm.OWNER.getValue().equals(acl.getPerm()) || Perm.EDIT.getValue().equals(acl.getPerm()))) {
            throw new RuntimeException("You do not have permission to update this variable system");
        }

        variableSystem.setVariableSystem(variableSystemUpdateRequest.getVariableSystem());
        variableSystem.setLastModifiedBy(currentUser.getEmail());
        variableSystem.setLastModifiedDate(Instant.now());
        variableSystemRepository.save(variableSystem);

        return new StandardResponse("Success", "Update variable system successfully");
    }

    public StandardResponse deleteVariableSystem(String id) {
        log.debug("Request to delete VariableSystem : {}", id);
        UserInFoDetails currentUser = getCurrentUser();

        VariableSystem variableSystem = variableSystemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("VariableSystem not found with id " + id));

        List<VariableSystemAcl> variableSystemAcls = variableSystemAclRepository.findAllByVarIdAndAssigneeId(variableSystem.getId(), currentUser.getId());
        if (variableSystemAcls.stream().noneMatch(acl -> Perm.OWNER.getValue().equals(acl.getPerm()))) {
            throw new RuntimeException("You do not have permission to delete this variable system");
        }

        variableSystemRepository.delete(variableSystem);
        variableSystemAclRepository.deleteAllByVarId((variableSystem.getId()));

        return new StandardResponse("Success", "Delete variable system successfully");
    }

    private List<VariableSystem> findAllByVariableSystemShare(
        Long id,
        List<Long> orgIds,
        Long userType,
        Long orgType,
        String perm
    ) {

        StringBuilder sql = new StringBuilder(
            "SELECT vs.* FROM variable_system vs\n" +
                "        JOIN variable_system_acl vsa ON vs.id = vsa.var_id\n" +
                "        WHERE vsa.authorizer_id != :id"
        );

        List<String> conditions = new ArrayList<>();

        if (id != null && userType != null) {
            conditions.add("(vsa.assignee_id = :id AND vsa.perm_type = :userType)");
        }
        if (orgIds != null && !orgIds.isEmpty() && orgType != null) {
            conditions.add("(vsa.assignee_id IN :orgIds AND vsa.perm_type = :orgType)");
        }

        if (perm != null) {
            sql.append(" AND vsa.perm != :perm");
        }

        if (conditions.isEmpty()) {
            return Collections.emptyList();
        }

        sql.append(" AND (").append(String.join(" OR ", conditions)).append(")");

        Query query = entityManager.createNativeQuery(sql.toString(), VariableSystem.class);
        query.setParameter("id", id);

        if (perm != null) {
            query.setParameter("perm", perm);
        }
        if (id != null && userType != null) {
            query.setParameter("id", id);
            query.setParameter("userType", userType);
        }
        if (orgIds != null && !orgIds.isEmpty() && orgType != null) {
            query.setParameter("orgIds", orgIds);
            query.setParameter("orgType", orgType);
        }

        @SuppressWarnings("unchecked")
        List<VariableSystem> result = query.getResultList();
        return result;
    }

    public StandardResponse getVariableSystemById(String id) {
        log.debug("Request to get VariableSystem by id : {}", id);
        UserInFoDetails currentUser = getCurrentUser();

        VariableSystem variableSystem = variableSystemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("VariableSystem not found with id " + id));

        List<VariableSystemAcl> variableSystemAcls = variableSystemAclRepository.findAllByVarIdAndAssigneeId(variableSystem.getId(), currentUser.getId());
        if (variableSystemAcls.stream().noneMatch(acl -> Perm.OWNER.getValue().equals(acl.getPerm()) || Perm.EDIT.getValue().equals(acl.getPerm()) || Perm.VIEW.getValue().equals(acl.getPerm()))) {
            throw new RuntimeException("You do not have permission to view this variable system");
        }

        return new StandardResponse("Success", variableSystem);
    }
}
