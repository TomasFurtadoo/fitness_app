package com.tomas.nutrition.dto;

import com.tomas.nutrition.model.Meal;

import java.time.LocalDate;
import java.util.List;

public record MealResponseDTO(
        Long id,
        String nome,
        Long userId,
        LocalDate data,
        List<MealItemResponseDTO> itens,
        NutrientesDTO totais
) {
    public static MealResponseDTO from(Meal meal) {
        List<MealItemResponseDTO> itens = meal.getItens().stream()
                .map(MealItemResponseDTO::from)
                .toList();
        return new MealResponseDTO(
                meal.getId(),
                meal.getNome(),
                meal.getUser().getId(),
                meal.getData(),
                itens,
                NutrientesDTO.from(meal.getTotals())
        );
    }
}