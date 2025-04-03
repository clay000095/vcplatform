package com.vcplatform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vcplatform.model.Investment;

public interface InvestmentRepository extends JpaRepository<Investment, Long> {
    List<Investment> findByInvestorId(Long investorId);
    List<Investment> findByProjectId(Long projectId);
}
