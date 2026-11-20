package com.tomas.nutrition.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NutrientesPorcaoTest {

    @Test
    @DisplayName("somar devolve um objeto novo com os valores somados")
    void somar() {
        NutrientesPorcao a = new NutrientesPorcao(100, 10, 20, 5);
        NutrientesPorcao b = new NutrientesPorcao(50, 5, 10, 2);

        assertThat(a.somar(b)).isEqualTo(new NutrientesPorcao(150, 15, 30, 7));
        // imutavel: o original nao muda
        assertThat(a).isEqualTo(new NutrientesPorcao(100, 10, 20, 5));
    }

    @Test
    @DisplayName("zero e o elemento neutro da soma")
    void zeroENeutro() {
        NutrientesPorcao a = new NutrientesPorcao(100, 10, 20, 5);

        assertThat(NutrientesPorcao.zero().somar(a)).isEqualTo(a);
        assertThat(a.somar(NutrientesPorcao.zero())).isEqualTo(a);
    }
}