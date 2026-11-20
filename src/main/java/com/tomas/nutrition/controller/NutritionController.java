package com.tomas.nutrition.controller;

import com.tomas.nutrition.dto.NutritionSummaryDTO;
import com.tomas.nutrition.service.NutritionEngineService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/users/{userId}/nutrition")
public class NutritionController {

    private final NutritionEngineService nutritionEngineService;

    public NutritionController(NutritionEngineService nutritionEngineService) {
        this.nutritionEngineService = nutritionEngineService;
    }

    /** GET /api/users/1/nutrition/summary  ou  ...?data=2026-10-15 (por defeito, hoje). */
    @GetMapping("/summary")
    public NutritionSummaryDTO dailySummary(
            @PathVariable Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return nutritionEngineService.getDailySummary(userId, data);
    }
}