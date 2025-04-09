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
            System.out.println("有資料啦");
            return;
        }

        User founder = new User(null, "founder@mail.com", "123", "FOUNDER");
        User founder1 = new User(null, "founder1@mail.com", "123", "FOUNDER");
        User investor = new User(null, "investor@mail.com", "123", "INVESTOR");
        User investor1 = new User(null, "investor1@mail.com", "123", "INVESTOR");
        User admin = new User(null, "admin@mail.com", "123", "ADMIN");
        User admin1 = new User(null, "admin1@mail.com", "123", "ADMIN");

        userRepo.saveAll(List.of(founder, founder1, investor, investor1, admin, admin1));

        // 科技類專案
        Project tech1 = new Project(null, "圖像生成平台", 
            "用戶只需輸入文字描述就能生成精美的圖片，適用於設計師、創作者和一般用戶。",
            new BigDecimal("200000"), BigDecimal.ZERO, "APPROVED", "TECH",
            founder.getId(), LocalDateTime.now());

        Project tech2 = new Project(null, "智能家居控制系統", 
            "整合所有智能家居設備的控制系統，提供語音控制場景設定。",
            new BigDecimal("300000"), BigDecimal.ZERO, "APPROVED", "TECH",
            founder.getId(), LocalDateTime.now());

        Project tech3 = new Project(null, "區塊鏈供應鏈追蹤", 
            "利用區塊鏈技術實現供應鏈全程追蹤，提高透明度和安全性。",
            new BigDecimal("800000"), BigDecimal.ZERO, "APPROVED", "TECH",
            founder.getId(), LocalDateTime.now());

        // 健康類專案
        Project health1 = new Project(null, "遠端醫療平台", 
            "提供線上問診、健康諮詢和處方藥配送服務的綜合醫療平台。",
            new BigDecimal("750000"), BigDecimal.ZERO, "APPROVED", "HEALTH",
            founder.getId(), LocalDateTime.now());

        Project health2 = new Project(null, "智能運動追蹤器", 
            "結合AI的運動追蹤設備，提供專業的運動建議和健康分析。",
            new BigDecimal("400000"), BigDecimal.ZERO, "APPROVED", "HEALTH",
            founder.getId(), LocalDateTime.now());

        Project health3 = new Project(null, "心理AI輔助評估", 
            "提供心理健康評估、諮詢和指導的應用程序。",
            new BigDecimal("200000"), BigDecimal.ZERO, "APPROVED", "HEALTH",
            founder.getId(), LocalDateTime.now());

        // 金融類專案
        Project finance1 = new Project(null, "智能投資顧問", 
            "使用AI分析市場數據，為個人投資者提供個性化投資建議。",
            new BigDecimal("600000"), BigDecimal.ZERO, "APPROVED", "FINANCE",
            founder.getId(), LocalDateTime.now());

        Project finance2 = new Project(null, "小額信貸平台", 
            "為中小企業提供快速的小額貸款服務。",
            new BigDecimal("900000"), BigDecimal.ZERO, "APPROVED", "FINANCE",
            founder.getId(), LocalDateTime.now());

        Project finance3 = new Project(null, "個人理財助手", 
            "幫助用戶管理個人財務、制定預算和實現理財目標的應用程序。",
            new BigDecimal("300000"), BigDecimal.ZERO, "APPROVED", "FINANCE",
            founder.getId(), LocalDateTime.now());

        // 教育類專案
        Project edu1 = new Project(null, "線上語言學習平台", 
            "提供互動式語言學習課程，支持多種語言和個性化學習路徑。",
            new BigDecimal("400000"), BigDecimal.ZERO, "APPROVED", "EDUCATION",
            founder.getId(), LocalDateTime.now());

        Project edu2 = new Project(null, "兒童編程教育", 
            "通過遊戲化方式教育兒童基礎程式知識。",
            new BigDecimal("250000"), BigDecimal.ZERO, "APPROVED", "EDUCATION",
            founder.getId(), LocalDateTime.now());

        Project edu3 = new Project(null, "職業技能培訓平台", 
            "提供各行業專業技能培訓課程的在線學習平台。",
            new BigDecimal("500000"), BigDecimal.ZERO, "APPROVED", "EDUCATION",
            founder.getId(), LocalDateTime.now());

        projectRepo.saveAll(List.of(
            tech1, tech2, tech3,
            health1, health2, health3,
            finance1, finance2, finance3,
            edu1, edu2, edu3
        ));

        // 添加一些投資記錄
        Investment inv1 = new Investment(null, investor.getId(), tech1.getId(),
                new BigDecimal("100000"), LocalDateTime.now());
        Investment inv2 = new Investment(null, investor.getId(), health1.getId(),
                new BigDecimal("200000"), LocalDateTime.now());
        Investment inv3 = new Investment(null, investor.getId(), finance1.getId(),
                new BigDecimal("150000"), LocalDateTime.now());

        investRepo.saveAll(List.of(inv1, inv2, inv3));

        System.out.println("測試資料初始化完成！");
    }
}
