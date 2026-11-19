package com.tomas.nutrition.dto;

import com.tomas.nutrition.model.Food;

public record FoodResponseDTO(
        Long id,
        String nome,
        double calorias100g,
        double proteina100g,
        double carbs100g,
        double gordura100g
) {
    public static FoodResponseDTO from(Food food) {
        return new FoodResponseDTO(
                food.getId(),
                food.getNome(),
                food.getCalorias100g(),
                food.getProteina100g(),
                food.getCarbs100g(),
                food.getGordura100g()
        );
    }
}