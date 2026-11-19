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

        NutrientesPorcao metas = user.getMetas();

        return new NutritionSummaryDTO(
                userId,
                dia,
                progresso(metas.calorias(), consumido.calorias()),
                progresso(metas.proteina(), consumido.proteina()),
                progresso(metas.carbs(), consumido.carbs()),
                progresso(metas.gordura(), consumido.gordura())
        );
    }

    private MacroProgressDTO progresso(double meta, double consumido) {
        double restante = meta - consumido;
        double percentagem = (meta > 0) ? (consumido / meta) * 100.0 : 0.0;
        return new MacroProgressDTO(meta, consumido, restante, percentagem);
    }
}