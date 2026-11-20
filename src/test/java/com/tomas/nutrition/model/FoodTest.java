package com.tomas.nutrition.model;

import com.tomas.nutrition.exception.InvalidDataException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

class FoodTest {

    private static Food arroz() {
        return new Food("Arroz cozido", 130, 2.7, 28.0, 0.3);
    }

    @Test
    @DisplayName("250g = fator 2.5 sobre os valores por 100g")
    void porcaoProporcional() {
        NutrientesPorcao n = arroz().getNutrientsByPortion(250);

        assertThat(n.calorias()).isCloseTo(325.0, within(0.001));
        assertThat(n.proteina()).isCloseTo(6.75, within(0.001));
        assertThat(n.carbs()).isCloseTo(70.0, within(0.001));
        assertThat(n.gordura()).isCloseTo(0.75, within(0.001));
    }

    @Test
    @DisplayName("100g devolve exatamente os valores base")
    void cemGramasDaOsValoresBase() {
        NutrientesPorcao n = arroz().getNutrientsByPortion(100);

        assertThat(n.calorias()).isCloseTo(130.0, within(0.001));
        assertThat(n.proteina()).isCloseTo(2.7, within(0.001));
    }

    @Test
    @DisplayName("50g devolve metade (fator < 1)")
    void meiaPorcao() {
        assertThat(arroz().getNutrientsByPortion(50).calorias()).isCloseTo(65.0, within(0.001));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -10.0})
    @DisplayName("Gramas nulas ou negativas sao rejeitadas")
    void gramasInvalidas(double gramas) {
        assertThatThrownBy(() -> arroz().getNutrientsByPortion(gramas))
                .isInstanceOf(InvalidDataException.class);
    }

    @Test
    @DisplayName("Valores nutricionais negativos sao rejeitados")
    void valoresNegativos() {
        assertThatThrownBy(() -> new Food("X", -1, 0, 0, 0)).isInstanceOf(InvalidDataException.class);
        assertThatThrownBy(() -> new Food("X", 0, -1, 0, 0)).isInstanceOf(InvalidDataException.class);
        assertThatThrownBy(() -> new Food("X", 0, 0, -1, 0)).isInstanceOf(InvalidDataException.class);
        assertThatThrownBy(() -> new Food("X", 0, 0, 0, -1)).isInstanceOf(InvalidDataException.class);
    }

    @Test
    @DisplayName("Zero e valido (ex: agua tem 0 kcal)")
    void zeroEValido() {
        assertThatCode(() -> new Food("Agua", 0, 0, 0, 0)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Nome vazio e rejeitado")
    void nomeVazio() {
        assertThatThrownBy(() -> new Food("  ", 10, 1, 1, 1)).isInstanceOf(InvalidDataException.class);
    }
}