package com.fis.fpt.repository;

import com.fis.fpt.domain.ApiInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ApiInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApiInfoRepository extends JpaRepository<ApiInfo, String> {
}
