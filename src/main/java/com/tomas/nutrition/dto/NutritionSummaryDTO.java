package com.tomas.nutrition.dto;

import java.time.LocalDate;

public record NutritionSummaryDTO(
        Long userId,
        LocalDate data,
        MacroProgressDTO calorias,
        MacroProgressDTO proteina,
        MacroProgressDTO carbs,
        MacroProgressDTO gordura
) {
}