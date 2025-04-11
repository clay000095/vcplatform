package com.vcplatform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vcplatform.model.Project;

import jakarta.persistence.LockModeType;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByFounderId(Long founderId);
    List<Project> findByStatus(String status);
    List<Project> findByCategory(String category);
    List<Project> findByCategoryAndStatus(String category, String status);
    
    // 使用悲觀鎖查詢專案，確保資金安全
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Project p WHERE p.id = :id")
    Optional<Project> findByIdWithLock(@Param("id") Long id);
}