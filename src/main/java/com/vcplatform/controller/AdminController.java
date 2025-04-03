package com.vcplatform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vcplatform.model.Project;
import com.vcplatform.service.ProjectService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/admin/projects")
@Tag(name = "管理員")
public class AdminController {
    @Autowired private ProjectService projectService;

    @GetMapping("/pending")
    public List<Project> pending() {
        return projectService.getPending();
    }

    @PostMapping("/{id}/approve")
    public Project approve(@PathVariable Long id) {
        return projectService.approve(id);
    }

    @PostMapping("/{id}/reject")
    public Project reject(@PathVariable Long id) {
        return projectService.reject(id);
    }
}

