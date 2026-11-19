package com.tomas.nutrition.service;

import com.tomas.nutrition.dto.MacroProgressDTO;
import com.tomas.nutrition.dto.NutritionSummaryDTO;
import com.tomas.nutrition.exception.ResourceNotFoundException;
import com.tomas.nutrition.model.Meal;
import com.tomas.nutrition.model.NutrientesPorcao;
import com.tomas.nutrition.model.User;
import com.tomas.nutrition.repository.MealRepository;
import com.tomas.nutrition.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class NutritionEngineService {

    // Distribuicao de macros (percentagem das calorias totais)
    private static final double PERCENT_PROTEINA = 0.30;
    private static final double PERCENT_CARBS = 0.40;
    private static final double PERCENT_GORDURA = 0.30;

    // Calorias por grama de cada macronutriente
    private static final double KCAL_POR_G_PROTEINA = 4.0;
    private static final double KCAL_POR_G_CARBS = 4.0;
    private static final double KCAL_POR_G_GORDURA = 9.0;

    private final UserRepository userRepository;
    private final MealRepository mealRepository;

    public NutritionEngineService(UserRepository userRepository, MealRepository mealRepository) {
        this.userRepository = userRepository;
        this.mealRepository = mealRepository;
    }

    @Transactional(readOnly = true)
    public NutritionSummaryDTO getDailySummary(Long userId, LocalDate data) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilizador", userId));
        LocalDate dia = (data != null) ? data : LocalDate.now();

        NutrientesPorcao consumido = mealRepository.findByUserIdAndData(userId, dia).stream()
                .map(Meal::getTotals)
                .reduce(NutrientesPorcao.zero(), NutrientesPorcao::somar);

        double tdee = user.getTdee();
        double metaProteina = tdee * PERCENT_PROTEINA / KCAL_POR_G_PROTEINA;
        double metaCarbs = tdee * PERCENT_CARBS / KCAL_POR_G_CARBS;
        double metaGordura = tdee * PERCENT_GORDURA / KCAL_POR_G_GORDURA;

        return new NutritionSummaryDTO(
                userId,
                dia,
                progresso(tdee, consumido.calorias()),
                progresso(metaProteina, consumido.proteina()),
                progresso(metaCarbs, consumido.carbs()),
                progresso(metaGordura, consumido.gordura())
        );
    }

    private MacroProgressDTO progresso(double meta, double consumido) {
        double restante = meta - consumido;
        double percentagem = (meta > 0) ? (consumido / meta) * 100.0 : 0.0;
        return new MacroProgressDTO(meta, consumido, restante, percentagem);
    }
}