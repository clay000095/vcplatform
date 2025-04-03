package com.vcplatform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vcplatform.model.Investment;
import com.vcplatform.service.InvestmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/investments")
@Tag(name = "投資人")
public class InvestmentController {
    @Autowired private InvestmentService investmentService;

    @PostMapping
    @Operation(summary = "投資專案")
    public Investment invest(@RequestBody Investment inv) {
        return investmentService.invest(inv);
    }

    @GetMapping("/my")
    public List<Investment> my(@RequestParam Long investorId) {
        return investmentService.getByInvestor(investorId);
    }
}

