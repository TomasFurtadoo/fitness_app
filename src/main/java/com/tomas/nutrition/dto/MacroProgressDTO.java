package com.tomas.nutrition.dto;

public record MacroProgressDTO(
        double meta,
        double consumido,
        double restante,
        double percentagem
) {
}