package com.vcplatform.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private BigDecimal targetAmount;
    private BigDecimal currentAmount = BigDecimal.ZERO;
    private String status = "PENDING"; // APPROVED / REJECTED
    private String category; // TECH, HEALTH, FINANCE, EDUCATION
    private Long founderId;
    private LocalDateTime createdAt = LocalDateTime.now();
}
