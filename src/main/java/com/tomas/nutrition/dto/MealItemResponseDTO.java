package com.tomas.nutrition.dto;

import com.tomas.nutrition.model.MealItem;

public record MealItemResponseDTO(
        Long id,
        Long foodId,
        String foodNome,
        double gramas,
        NutrientesDTO nutrientes
) {
    public static MealItemResponseDTO from(MealItem item) {
        return new MealItemResponseDTO(
                item.getId(),
                item.getFood().getId(),
                item.getFood().getNome(),
                item.getGramas(),
                NutrientesDTO.from(item.getNutrientes())
        );
    }
}