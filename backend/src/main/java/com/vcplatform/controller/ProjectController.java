package com.vcplatform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vcplatform.model.Investment;
import com.vcplatform.model.Project;
import com.vcplatform.service.InvestmentService;
import com.vcplatform.service.ProjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "創業者與專案")
public class ProjectController {
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired private ProjectService projectService;

    @PostMapping
    @Operation(summary = "創建新專案")
    public Project create(@RequestBody Project p) {
        return projectService.create(p);
    }

    @GetMapping("/my")
    public List<Project> my(@RequestParam Long founderId) {
        return projectService.getByFounder(founderId);
    }

    @GetMapping
    public List<Project> approved() {
        logger.info("Fetching approved projects");
        try {
            List<Project> projects = projectService.getApproved();
            logger.info("Found {} approved projects", projects.size());
            return projects;
        } catch (Exception e) {
            logger.error("Error fetching approved projects", e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public Project get(@PathVariable Long id) {
        return projectService.get(id).orElseThrow();
    }

    @GetMapping("/{id}/investors")
    public List<Investment> investors(@PathVariable Long id, @Autowired InvestmentService investService) {
        return investService.getByProject(id);
    }
    
    @GetMapping("/category/{category}")
    @Operation(summary = "按類別獲取項目")
    public List<Project> getByCategory(@PathVariable String category) {
        logger.info("Fetching projects by category: {}", category);
        try {
            List<Project> projects = projectService.getByCategory(category);
            logger.info("Found {} projects in category {}", projects.size(), category);
            return projects;
        } catch (Exception e) {
            logger.error("Error fetching projects by category: {}", category, e);
            throw e;
        }
    }
}
