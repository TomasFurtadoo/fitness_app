package com.tomas.nutrition.dto;

import com.tomas.nutrition.model.NutrientesPorcao;

public record NutrientesDTO(
        double calorias,
        double proteina,
        double carbs,
        double gordura
) {
    public static NutrientesDTO from(NutrientesPorcao n) {
        return new NutrientesDTO(n.calorias(), n.proteina(), n.carbs(), n.gordura());
    }
}