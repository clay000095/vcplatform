package com.vcplatform.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vcplatform.exception.InvestmentException;
import com.vcplatform.model.Investment;
import com.vcplatform.model.Project;
import com.vcplatform.repository.InvestmentRepository;
import com.vcplatform.repository.ProjectRepository;

@Service
public class InvestmentService {
    @Autowired private InvestmentRepository investRepo;
    @Autowired private ProjectRepository projectRepo;

    @Transactional
    public Investment invest(Investment inv) {
        // 使用悲觀鎖查詢專案
        Project project = projectRepo.findByIdWithLock(inv.getProjectId())
            .orElseThrow(() -> new InvestmentException("專案不存在"));

        // 檢查專案狀態
        if (!"APPROVED".equals(project.getStatus())) {
            throw new InvestmentException("專案尚未核准，無法投資");
        }

        // 計算投資後的金額
        BigDecimal newAmount = project.getCurrentAmount().add(inv.getAmount());
        
        // 檢查是否超過目標金額
        if (newAmount.compareTo(project.getTargetAmount()) > 0) {
            throw new InvestmentException("投資金額將超過目標金額");
        }

        // 更新專案金額
        project.setCurrentAmount(newAmount);
        projectRepo.save(project);

        // 保存投資記錄
        return investRepo.save(inv);
    }

    public List<Investment> getByInvestor(Long id) {
        return investRepo.findByInvestorId(id);
    }

    public List<Investment> getByProject(Long id) {
        return investRepo.findByProjectId(id);
    }
}

