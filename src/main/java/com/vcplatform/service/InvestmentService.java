package com.vcplatform.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vcplatform.model.Investment;
import com.vcplatform.model.Project;
import com.vcplatform.repository.InvestmentRepository;
import com.vcplatform.repository.ProjectRepository;

@Service
public class InvestmentService {
    @Autowired private InvestmentRepository investRepo;
    @Autowired private ProjectRepository projectRepo;

    public Investment invest(Investment inv) {
        Project p = projectRepo.findById(inv.getProjectId()).orElseThrow();
        p.setCurrentAmount(p.getCurrentAmount().add(inv.getAmount()));
        projectRepo.save(p);
        return investRepo.save(inv);
    }

    public List<Investment> getByInvestor(Long id) {
        return investRepo.findByInvestorId(id);
    }

    public List<Investment> getByProject(Long id) {
        return investRepo.findByProjectId(id);
    }
}

