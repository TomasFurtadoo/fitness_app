package com.tomas.nutrition.service;

import com.tomas.nutrition.dto.NutritionSummaryDTO;
import com.tomas.nutrition.exception.ResourceNotFoundException;
import com.tomas.nutrition.model.Food;
import com.tomas.nutrition.model.Genero;
import com.tomas.nutrition.model.Meal;
import com.tomas.nutrition.model.NivelAtividade;
import com.tomas.nutrition.model.User;
import com.tomas.nutrition.repository.MealRepository;
import com.tomas.nutrition.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NutritionEngineServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MealRepository mealRepository;

    private NutritionEngineService service;

    private final LocalDate dia = LocalDate.of(2026, 10, 15);
    private User user;
    private Food arroz;
    private Food frango;
    private Food banana;

    @BeforeEach
    void setUp() {
        service = new NutritionEngineService(userRepository, mealRepository);
        user = new User("Tomasio", 22, 75.0, 178.0, Genero.MASCULINO, NivelAtividade.MODERADAMENTE_ATIVO);
        arroz = new Food("Arroz cozido", 130, 2.7, 28.0, 0.3);
        frango = new Food("Peito de frango cozido", 165, 31.0, 0.0, 3.6);
        banana = new Food("Banana", 89, 1.1, 22.8, 0.3);
    }

    @Test
    @DisplayName("Soma o consumido de todas as refeicoes do dia e compara com as metas")
    void somaRefeicoesDoDia() {
        Meal almoco = new Meal("Almoco", user, dia);
        almoco.addFood(arroz, 250);   // 325 kcal, 6.75 P
        almoco.addFood(frango, 150);  // 247.5 kcal, 46.5 P
        Meal lanche = new Meal("Lanche", user, dia);
        lanche.addFood(banana, 200);  // 178 kcal, 2.2 P

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mealRepository.findByUserIdAndData(1L, dia)).thenReturn(List.of(almoco, lanche));

        NutritionSummaryDTO resumo = service.getDailySummary(1L, dia);

        // calorias: 325 + 247.5 + 178 = 750.5 ; meta = TDEE = 2724.125
        assertThat(resumo.calorias().consumido()).isCloseTo(750.5, within(0.001));
        assertThat(resumo.calorias().meta()).isCloseTo(2724.125, within(0.001));
        assertThat(resumo.calorias().restante()).isCloseTo(1973.625, within(0.001));
        assertThat(resumo.calorias().percentagem()).isCloseTo(27.55, within(0.01));

        // proteina: 6.75 + 46.5 + 2.2 = 55.45 ; meta = 75 * 1.8 = 135
        assertThat(resumo.proteina().consumido()).isCloseTo(55.45, within(0.001));
        assertThat(resumo.proteina().meta()).isCloseTo(135.0, within(0.001));
        assertThat(resumo.proteina().restante()).isCloseTo(79.55, within(0.001));

        assertThat(resumo.userId()).isEqualTo(1L);
        assertThat(resumo.data()).isEqualTo(dia);
    }

    @Test
    @DisplayName("Sem refeicoes: consumido 0, restante = meta, percentagem 0")
    void semRefeicoes() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mealRepository.findByUserIdAndData(1L, dia)).thenReturn(List.of());

        NutritionSummaryDTO resumo = service.getDailySummary(1L, dia);

        assertThat(resumo.calorias().consumido()).isEqualTo(0.0);
        assertThat(resumo.calorias().restante()).isCloseTo(resumo.calorias().meta(), within(0.001));
        assertThat(resumo.calorias().percentagem()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Passar a meta da restante negativo e percentagem acima de 100")
    void passouAMeta() {
        Meal excesso = new Meal("Banquete", user, dia);
        excesso.addFood(new Food("Azeite", 884, 0, 0, 100), 400); // 3536 kcal > 2724 kcal

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mealRepository.findByUserIdAndData(1L, dia)).thenReturn(List.of(excesso));

        NutritionSummaryDTO resumo = service.getDailySummary(1L, dia);

        assertThat(resumo.calorias().restante()).isNegative();
        assertThat(resumo.calorias().percentagem()).isGreaterThan(100.0);
    }

    @Test
    @DisplayName("Utilizador inexistente lanca ResourceNotFoundException sem consultar refeicoes")
    void utilizadorInexistente() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getDailySummary(99L, dia))
                .isInstanceOf(ResourceNotFoundException.class);

        verifyNoInteractions(mealRepository);
    }
}