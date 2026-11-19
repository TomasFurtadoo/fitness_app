package com.tomas.nutrition.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MealItemRequestDTO(
        @NotNull(message = "O id do alimento e obrigatorio.")
        Long foodId,

        @NotNull(message = "A quantidade em gramas e obrigatoria.")
        @Positive(message = "A quantidade em gramas deve ser positiva.")
        Double gramas
) {
}