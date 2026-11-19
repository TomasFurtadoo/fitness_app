package com.tomas.nutrition.dto;

import com.tomas.nutrition.model.Genero;
import com.tomas.nutrition.model.NivelAtividade;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequestDTO(
        @NotBlank(message = "O nome nao pode ser vazio.")
        String nome,

        @NotNull(message = "A idade e obrigatoria.")
        @Min(value = 1, message = "A idade deve ser no minimo 1.")
        @Max(value = 120, message = "A idade deve ser no maximo 120.")
        Integer idade,

        @NotNull(message = "O peso e obrigatorio.")
        @DecimalMin(value = "20", inclusive = false, message = "O peso deve ser superior a 20kg.")
        Double peso,

        @NotNull(message = "A altura e obrigatoria.")
        @DecimalMin(value = "50", message = "A altura deve ser no minimo 50cm.")
        @DecimalMax(value = "250", message = "A altura deve ser no maximo 250cm.")
        Double altura,

        @NotNull(message = "O genero e obrigatorio.")
        Genero genero,

        @NotNull(message = "O nivel de atividade e obrigatorio.")
        NivelAtividade nivelAtividade
) {
}