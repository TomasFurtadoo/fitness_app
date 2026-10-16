package com.tomas.nutrition.model;

public record NutrientesPorcao(
        double calorias,
        double proteina,
        double carbs,
        double gordura
) {

    public NutrientesPorcao somar(NutrientesPorcao outro) {
        return new NutrientesPorcao(
                this.calorias + outro.calorias,
                this.proteina + outro.proteina,
                this.carbs + outro.carbs,
                this.gordura + outro.gordura
        );
    }

    public static NutrientesPorcao zero() {
        return new NutrientesPorcao(0, 0, 0, 0);
    }
}