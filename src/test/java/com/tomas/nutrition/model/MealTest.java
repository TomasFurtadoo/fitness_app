package com.tomas.nutrition.model;

import com.tomas.nutrition.exception.InvalidDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

class MealTest {

    private User user;
    private Meal meal;
    private Food arroz;
    private Food frango;

    @BeforeEach
    void setUp() {
        user = new User("Tomasio", 22, 75.0, 178.0, Genero.MASCULINO, NivelAtividade.MODERADAMENTE_ATIVO);
        meal = new Meal("Almoco", user, LocalDate.of(2026, 10, 15));
        arroz = new Food("Arroz cozido", 130, 2.7, 28.0, 0.3);
        frango = new Food("Peito de frango cozido", 165, 31.0, 0.0, 3.6);
    }

    @Test
    @DisplayName("Refeicao vazia tem totais a zero")
    void refeicaoVazia() {
        assertThat(meal.getTotals()).isEqualTo(NutrientesPorcao.zero());
    }

    @Test
    @DisplayName("getTotals soma os nutrientes de todos os itens")
    void totaisSomamItens() {
        meal.addFood(arroz, 250);   // 325 kcal, 6.75 P, 70 C, 0.75 G
        meal.addFood(frango, 150);  // 247.5 kcal, 46.5 P, 0 C, 5.4 G

        NutrientesPorcao total = meal.getTotals();

        assertThat(total.calorias()).isCloseTo(572.5, within(0.001));
        assertThat(total.proteina()).isCloseTo(53.25, within(0.001));
        assertThat(total.carbs()).isCloseTo(70.0, within(0.001));
        assertThat(total.gordura()).isCloseTo(6.15, within(0.001));
    }

    @Test
    @DisplayName("removeFood remove o alimento e atualiza os totais")
    void removerAlimento() {
        meal.addFood(arroz, 250);
        meal.addFood(frango, 150);

        meal.removeFood(arroz);

        assertThat(meal.getItens()).hasSize(1);
        assertThat(meal.getTotals().calorias()).isCloseTo(247.5, within(0.001));
    }

    @Test
    @DisplayName("removeFood de um alimento ausente nao altera nada")
    void removerAlimentoAusente() {
        meal.addFood(arroz, 250);

        meal.removeFood(frango);

        assertThat(meal.getItens()).hasSize(1);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -50.0})
    @DisplayName("addFood rejeita gramas nulas ou negativas e nao adiciona o item")
    void addFoodGramasInvalidas(double gramas) {
        assertThatThrownBy(() -> meal.addFood(arroz, gramas))
                .isInstanceOf(InvalidDataException.class);
        assertThat(meal.getItens()).isEmpty();
    }

    @Test
    @DisplayName("Uma refeicao tem de pertencer a um utilizador")
    void refeicaoSemUtilizador() {
        assertThatThrownBy(() -> new Meal("Almoco", null, LocalDate.now()))
                .isInstanceOf(InvalidDataException.class);
    }

    @Test
    @DisplayName("Data e nome sao obrigatorios")
    void dataENomeObrigatorios() {
        assertThatThrownBy(() -> new Meal("Almoco", user, null)).isInstanceOf(InvalidDataException.class);
        assertThatThrownBy(() -> new Meal(" ", user, LocalDate.now())).isInstanceOf(InvalidDataException.class);
    }
}