package com.tomas.nutrition.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record MealRequestDTO(
        @NotBlank(message = "O nome da refeicao nao pode ser vazio.")
        String nome,

        @NotNull(message = "O id do utilizador e obrigatorio.")
        Long userId,

        LocalDate data   // opcional: se vier null, o service usa a data de hoje
) {
}