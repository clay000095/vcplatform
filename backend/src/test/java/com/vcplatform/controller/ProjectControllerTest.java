package com.vcplatform.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vcplatform.model.Project;
import com.vcplatform.service.ProjectService;

@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    private Project testProject;

    @BeforeEach
    public void setUp() {
        testProject = new Project();
        testProject.setId(1L);
        testProject.setTitle("Test Project");
        testProject.setDescription("Test Description");
        testProject.setCategory("Technology");
        testProject.setStatus("PENDING");
    }

    /**
     * 測試創建項目的 API 端點
     * 驗證：
     * 1. 可以成功發送 POST 請求
     * 2. 返回 200 OK 狀態碼
     * 3. 返回的項目包含正確的 ID 和標題
     */
    @Test
    @DisplayName("Should create project successfully")
    public void testCreateProject_Success() throws Exception {
        when(projectService.create(any(Project.class))).thenReturn(testProject);

        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProject)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testProject.getId()))
                .andExpect(jsonPath("$.title").value(testProject.getTitle()));
    }

    /**
     * 測試獲取創始人項目的 API 端點
     * 驗證：
     * 1. 可以成功發送 GET 請求
     * 2. 返回 200 OK 狀態碼
     * 3. 返回的項目列表包含正確的項目信息
     */
    @Test
    @DisplayName("Should get projects by founder ID")
    public void testGetByFounder_Success() throws Exception {
        when(projectService.getByFounder(1L)).thenReturn(Arrays.asList(testProject));

        mockMvc.perform(get("/api/projects/my")
                .param("founderId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testProject.getId()))
                .andExpect(jsonPath("$[0].title").value(testProject.getTitle()));
    }

    /**
     * 測試獲取已批准項目的 API 端點
     * 驗證：
     * 1. 可以成功發送 GET 請求
     * 2. 返回 200 OK 狀態碼
     * 3. 返回的項目列表包含正確的已批准項目
     */
    @Test
    @DisplayName("Should get approved projects")
    public void testGetApproved_Success() throws Exception {
        when(projectService.getApproved()).thenReturn(Arrays.asList(testProject));

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testProject.getId()))
                .andExpect(jsonPath("$[0].title").value(testProject.getTitle()));
    }

    /**
     * 測試按類別獲取項目的 API 端點
     * 驗證：
     * 1. 可以成功發送 GET 請求
     * 2. 返回 200 OK 狀態碼
     * 3. 返回的項目列表包含正確類別的項目
     */
    @Test
    @DisplayName("Should get projects by category")
    public void testGetByCategory_Success() throws Exception {
        when(projectService.getByCategory("Technology")).thenReturn(Arrays.asList(testProject));

        mockMvc.perform(get("/api/projects/category/Technology"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testProject.getId()))
                .andExpect(jsonPath("$[0].title").value(testProject.getTitle()));
    }

    /**
     * 測試獲取單個項目的 API 端點（項目存在的情況）
     * 驗證：
     * 1. 可以成功發送 GET 請求
     * 2. 返回 200 OK 狀態碼
     * 3. 返回的項目包含正確的信息
     */
    @Test
    @DisplayName("Should get project by ID when exists")
    public void testGetProjectById_Success() throws Exception {
        when(projectService.get(1L)).thenReturn(Optional.of(testProject));

        mockMvc.perform(get("/api/projects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testProject.getId()))
                .andExpect(jsonPath("$.title").value(testProject.getTitle()));
    }


    /**
     * 測試批准項目的 API 端點（項目不存在的情況）
     * 驗證：
     * 1. 可以成功發送 PUT 請求
     * 2. 返回 404 Not Found 狀態碼
     */
    @Test
    @DisplayName("Should return 404 when approving non-existent project")
    public void testApproveProject_NotFound() throws Exception {
        when(projectService.get(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/projects/1/approve"))
                .andExpect(status().isNotFound());
    }


    /**
     * 測試拒絕項目的 API 端點（項目不存在的情況）
     * 驗證：
     * 1. 可以成功發送 PUT 請求
     * 2. 返回 404 Not Found 狀態碼
     */
    @Test
    @DisplayName("Should return 404 when rejecting non-existent project")
    public void testRejectProject_NotFound() throws Exception {
        when(projectService.get(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/projects/1/reject"))
                .andExpect(status().isNotFound());
    }
}