package com.vcplatform.datatest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.vcplatform.model.Investment;
import com.vcplatform.model.Project;
import com.vcplatform.model.User;
import com.vcplatform.repository.InvestmentRepository;
import com.vcplatform.repository.ProjectRepository;
import com.vcplatform.repository.UserRepository;

@Component
public class DataTester implements CommandLineRunner {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    private InvestmentRepository investRepo;

    @Override
    public void run(String... args) {
        if (userRepo.count() > 0) {
            System.out.println("✅ 已有資料，跳過測資初始化");
            return;
        }

        // 👤 使用者
        User founder = new User(null, "founder@mail.com", "123", "FOUNDER");
        User investor = new User(null, "investor@mail.com", "123", "INVESTOR");
        User admin = new User(null, "admin@mail.com", "123", "ADMIN");

        userRepo.saveAll(List.of(founder, investor, admin));

        // 💼 專案
        Project p1 = new Project(null, "AI 圖像生成平台", "一句話產生精美圖片",
                new BigDecimal("50000"), BigDecimal.ZERO, "PENDING",
                founder.getId(), LocalDateTime.now());

        Project p2 = new Project(null, "自動財報系統", "幫助企業快速生成財報",
                new BigDecimal("80000"), BigDecimal.ZERO, "APPROVED",
                founder.getId(), LocalDateTime.now());

        projectRepo.saveAll(List.of(p1, p2));

        // 💰 投資紀錄
        Investment inv1 = new Investment(null, investor.getId(), p2.getId(),
                new BigDecimal("20000"), LocalDateTime.now());

        investRepo.save(inv1);

        System.out.println("🚀 測試資料初始化完成！");
    }
}
