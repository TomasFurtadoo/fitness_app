package com.tomas.nutrition.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record FoodRequestDTO(
        @NotBlank(message = "O nome do alimento nao pode ser vazio.")
        String nome,

        @NotNull(message = "As calorias sao obrigatorias.")
        @PositiveOrZero(message = "As calorias nao podem ser negativas.")
        Double calorias100g,

        @NotNull(message = "A proteina e obrigatoria.")
        @PositiveOrZero(message = "A proteina nao pode ser negativa.")
        Double proteina100g,

        @NotNull(message = "Os carboidratos sao obrigatorios.")
        @PositiveOrZero(message = "Os carboidratos nao podem ser negativos.")
        Double carbs100g,

        @NotNull(message = "A gordura e obrigatoria.")
        @PositiveOrZero(message = "A gordura nao pode ser negativa.")
        Double gordura100g
) {
}