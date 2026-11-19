package com.tomas.nutrition.service;

import com.tomas.nutrition.dto.MealItemRequestDTO;
import com.tomas.nutrition.dto.MealRequestDTO;
import com.tomas.nutrition.dto.MealResponseDTO;
import com.tomas.nutrition.exception.ResourceNotFoundException;
import com.tomas.nutrition.model.Food;
import com.tomas.nutrition.model.Meal;
import com.tomas.nutrition.model.User;
import com.tomas.nutrition.repository.FoodRepository;
import com.tomas.nutrition.repository.MealRepository;
import com.tomas.nutrition.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MealService {

    private final MealRepository mealRepository;
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;

    public MealService(MealRepository mealRepository,
                       UserRepository userRepository,
                       FoodRepository foodRepository) {
        this.mealRepository = mealRepository;
        this.userRepository = userRepository;
        this.foodRepository = foodRepository;
    }

    @Transactional
    public MealResponseDTO create(MealRequestDTO dto) {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilizador", dto.userId()));
        LocalDate data = (dto.data() != null) ? dto.data() : LocalDate.now();
        Meal meal = new Meal(dto.nome(), user, data);
        return MealResponseDTO.from(mealRepository.save(meal));
    }

    @Transactional(readOnly = true)
    public MealResponseDTO findById(Long id) {
        return MealResponseDTO.from(getMeal(id));
    }

    @Transactional(readOnly = true)
    public List<MealResponseDTO> findByUser(Long userId, LocalDate data) {
        List<Meal> refeicoes = (data != null)
                ? mealRepository.findByUserIdAndData(userId, data)
                : mealRepository.findByUserId(userId);
        return refeicoes.stream().map(MealResponseDTO::from).toList();
    }

    @Transactional
    public MealResponseDTO addItem(Long mealId, MealItemRequestDTO dto) {
        Meal meal = getMeal(mealId);
        Food food = foodRepository.findById(dto.foodId())
                .orElseThrow(() -> new ResourceNotFoundException("Alimento", dto.foodId()));
        meal.addFood(food, dto.gramas());
        return MealResponseDTO.from(mealRepository.saveAndFlush(meal));
    }

    @Transactional
    public MealResponseDTO removeItem(Long mealId, Long foodId) {
        Meal meal = getMeal(mealId);
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new ResourceNotFoundException("Alimento", foodId));
        boolean presente = meal.getItens().stream()
                .anyMatch(item -> item.getFood().equals(food));
        if (!presente) {
            throw new ResourceNotFoundException(
                    "O alimento " + foodId + " nao existe na refeicao " + mealId + ".");
        }
        meal.removeFood(food);
        return MealResponseDTO.from(mealRepository.saveAndFlush(meal));
    }

    @Transactional
    public void delete(Long id) {
        mealRepository.delete(getMeal(id));
    }

    private Meal getMeal(Long id) {
        return mealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Refeicao", id));
    }
}