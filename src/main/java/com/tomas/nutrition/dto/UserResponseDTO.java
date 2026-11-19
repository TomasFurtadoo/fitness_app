package com.tomas.nutrition.dto;

import com.tomas.nutrition.model.Genero;
import com.tomas.nutrition.model.NivelAtividade;
import com.tomas.nutrition.model.User;

public record UserResponseDTO(
        Long id,
        String nome,
        int idade,
        double peso,
        double altura,
        Genero genero,
        NivelAtividade nivelAtividade,
        double proteinaGPorKg,
        double gorduraPercent,
        double imc,
        String classificacaoImc,
        double tmb,
        double tdee,
        NutrientesDTO metas
) {
    public static UserResponseDTO from(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getNome(),
                user.getIdade(),
                user.getPeso(),
                user.getAltura(),
                user.getGenero(),
                user.getNivelAtividade(),
                user.getProteinaGPorKg(),
                user.getGorduraPercent(),
                user.getImc(),
                user.getClassificacaoImc().getDescricao(),
                user.getTmb(),
                user.getTdee(),
                NutrientesDTO.from(user.getMetas())
        );
    }
}