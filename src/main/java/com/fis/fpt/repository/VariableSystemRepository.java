package com.fis.fpt.repository;

import com.fis.fpt.domain.VariableSystem;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the VariableSystem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VariableSystemRepository extends JpaRepository<VariableSystem, String> {
    List<VariableSystem> findAllByUserId(Long userId);
}
