package com.fis.fpt.repository;

import com.fis.fpt.domain.StorageFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the StorageFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StorageFileRepository extends JpaRepository<StorageFile, String> {
}
