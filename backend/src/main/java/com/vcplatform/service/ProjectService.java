package com.vcplatform.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vcplatform.model.Project;
import com.vcplatform.repository.ProjectRepository;

@Service
public class ProjectService {
    @Autowired private ProjectRepository projectRepo;

    public Project create(Project project) {
        return projectRepo.save(project);
    }

    public List<Project> getByFounder(Long founderId) {
        return projectRepo.findByFounderId(founderId);
    }

    public List<Project> getApproved() {
        return projectRepo.findByStatus("APPROVED");
    }

    public List<Project> getPending() {
        return projectRepo.findByStatus("PENDING");
    }

    public Optional<Project> get(Long id) {
        return projectRepo.findById(id);
    }

    public Project approve(Long id) {
        Project p = projectRepo.findById(id).orElseThrow();
        p.setStatus("APPROVED");
        return projectRepo.save(p);
    }

    public Project reject(Long id) {
        Project p = projectRepo.findById(id).orElseThrow();
        p.setStatus("REJECTED");
        return projectRepo.save(p);
    }
}

