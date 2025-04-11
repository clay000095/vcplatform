package com.vcplatform.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.vcplatform.exception.InvestmentException;
import com.vcplatform.model.Investment;
import com.vcplatform.model.Project;
import com.vcplatform.repository.ProjectRepository;

@SpringBootTest
public class InvestmentServiceTest {

    @Autowired
    private InvestmentService investmentService;

    @Autowired
    private ProjectRepository projectRepository;

    private Project testProject;

    @BeforeEach
    void setUp() {
        // 創建測試用專案
        testProject = new Project();
        testProject.setTitle("測試專案");
        testProject.setDescription("用於測試悲觀鎖");
        testProject.setTargetAmount(new BigDecimal("100000")); // 目標金額10萬
        testProject.setCurrentAmount(BigDecimal.ZERO);
        testProject.setStatus("APPROVED");
        testProject.setFounderId(1L);
        testProject = projectRepository.save(testProject);
    }

    @Test
    void testConcurrentInvestment() throws InterruptedException {
        int numberOfThreads = 10;
        BigDecimal investAmount = new BigDecimal("20000"); // 每次投資2萬
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        // 執行10個並發投資
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.execute(() -> {
                try {
                    Investment investment = new Investment();
                    investment.setInvestorId(1L);
                    investment.setProjectId(testProject.getId());
                    investment.setAmount(investAmount);

                    investmentService.invest(investment);
                    successCount.incrementAndGet();
                } catch (InvestmentException e) {
                    failCount.incrementAndGet();
                    System.out.println("投資失敗: " + e.getMessage());
                } catch (Exception e) {
                    failCount.incrementAndGet();
                    System.out.println("未預期的錯誤: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        // 等待所有線程完成
        latch.await();
        executorService.shutdown();

        // 重新獲取專案狀態
        Project updatedProject = projectRepository.findById(testProject.getId()).orElseThrow();

        // 驗證結果
        System.out.println("=== 測試結果 ===");
        System.out.println("目標金額: " + testProject.getTargetAmount());
        System.out.println("最終金額: " + updatedProject.getCurrentAmount());
        System.out.println("成功投資次數: " + successCount.get());
        System.out.println("失敗投資次數: " + failCount.get());

        // 斷言檢查
        assertTrue(updatedProject.getCurrentAmount().compareTo(testProject.getTargetAmount()) <= 0, 
            "最終金額不應超過目標金額");
        assertEquals(numberOfThreads, successCount.get() + failCount.get(), 
            "成功和失敗的總數應等於執行的線程數");
        
        // 檢查投資金額是否正確
        BigDecimal expectedAmount = investAmount.multiply(BigDecimal.valueOf(successCount.get()));
        assertEquals(0, updatedProject.getCurrentAmount().compareTo(expectedAmount), 
            "最終金額應等於成功投資次數乘以投資金額");
    }

    @Test
    void testInvestmentExceedingTarget() {
        // 創建一個接近目標金額的專案
        Project nearLimitProject = new Project();
        nearLimitProject.setTitle("接近上限測試專案");
        nearLimitProject.setTargetAmount(new BigDecimal("100000"));
        nearLimitProject.setCurrentAmount(new BigDecimal("90000")); // 已募集9萬
        nearLimitProject.setStatus("APPROVED");
        nearLimitProject.setFounderId(1L);
        nearLimitProject = projectRepository.save(nearLimitProject);

        // 嘗試投資超過剩餘金額
        Investment investment = new Investment();
        investment.setInvestorId(1L);
        investment.setProjectId(nearLimitProject.getId());
        investment.setAmount(new BigDecimal("20000")); // 嘗試投資2萬

        // 驗證是否拋出異常
        assertThrows(InvestmentException.class, () -> {
            investmentService.invest(investment);
        }, "應該拋出異常，因為投資金額會超過目標金額");
    }
} 