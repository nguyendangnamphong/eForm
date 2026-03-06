package com.fis.fpt.repository;

import com.fis.fpt.domain.VariableSystem;
import com.fis.fpt.domain.VariableSystemAcl;

import com.google.common.collect.FluentIterable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the VariableSystemAcl entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VariableSystemAclRepository extends JpaRepository<VariableSystemAcl, Long> {
    VariableSystemAcl findByVarIdAndAssigneeId(String varId, Long assigneeId);

    List<VariableSystemAcl> findAllByVarIdAndAssigneeId(String varId, Long assigneeId);

    VariableSystemAcl findById(String id);

    void deleteAllByVarId(String varId);

    List<VariableSystemAcl> findByVarIdAndPermIn(String varId, Collection<String> perms);

    @Query(value = "SELECT * FROM variable_system_acl vsa WHERE vsa.var_id = :varId AND vsa.perm = :perm", nativeQuery = true)
    Optional<VariableSystemAcl> findOwnerByVarId(@Param(value = "varId")String varId,@Param(value = "perm") String perm);

    @Query(value = "SELECT * FROM variable_system_acl vsa WHERE vsa.var_id IN :ids AND vsa.perm NOT IN :perm", nativeQuery = true)
    List<VariableSystemAcl> findByVarIdInAndIgnorePermIn(@Param(value = "ids") List<String> ids, @Param(value = "perm") List<String> perms);

    List<VariableSystemAcl> findAllByIdIn(Collection<String> ids);

    List<VariableSystemAcl> findAllByVarId(String varId);
}
