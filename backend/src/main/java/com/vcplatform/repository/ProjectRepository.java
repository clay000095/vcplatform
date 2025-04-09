package com.vcplatform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vcplatform.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByFounderId(Long founderId);
    List<Project> findByStatus(String status);
    List<Project> findByCategory(String category);
    List<Project> findByCategoryAndStatus(String category, String status);  
}