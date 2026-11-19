package com.tomas.nutrition.dto;

import jakarta.validation.constraints.NotBlank;

public record MealRequestDTO(
        @NotBlank(message = "O nome da refeicao nao pode ser vazio.")
        String nome
) {
}