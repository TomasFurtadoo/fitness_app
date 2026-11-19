package com.tomas.nutrition.service;

import com.tomas.nutrition.dto.FoodRequestDTO;
import com.tomas.nutrition.dto.FoodResponseDTO;
import com.tomas.nutrition.exception.ResourceNotFoundException;
import com.tomas.nutrition.model.Food;
import com.tomas.nutrition.repository.FoodRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FoodService {

    private final FoodRepository foodRepository;

    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    @Transactional
    public FoodResponseDTO create(FoodRequestDTO dto) {
        Food food = new Food(dto.nome(), dto.calorias100g(), dto.proteina100g(),
                dto.carbs100g(), dto.gordura100g());
        return FoodResponseDTO.from(foodRepository.save(food));
    }

    @Transactional(readOnly = true)
    public FoodResponseDTO findById(Long id) {
        return FoodResponseDTO.from(getFood(id));
    }

    @Transactional(readOnly = true)
    public List<FoodResponseDTO> findAll() {
        return foodRepository.findAll().stream()
                .map(FoodResponseDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FoodResponseDTO> searchByNome(String nome) {
        return foodRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(FoodResponseDTO::from)
                .toList();
    }

    @Transactional
    public FoodResponseDTO update(Long id, FoodRequestDTO dto) {
        Food food = getFood(id);
        food.setNome(dto.nome());
        food.setCalorias100g(dto.calorias100g());
        food.setProteina100g(dto.proteina100g());
        food.setCarbs100g(dto.carbs100g());
        food.setGordura100g(dto.gordura100g());
        return FoodResponseDTO.from(food);
    }

    @Transactional
    public void delete(Long id) {
        foodRepository.delete(getFood(id));
    }

    private Food getFood(Long id) {
        return foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alimento", id));
    }
}