package com.tomas.nutrition.service;

import com.tomas.nutrition.dto.UserRequestDTO;
import com.tomas.nutrition.dto.UserResponseDTO;
import com.tomas.nutrition.exception.ResourceNotFoundException;
import com.tomas.nutrition.model.Meal;
import com.tomas.nutrition.model.User;
import com.tomas.nutrition.repository.MealRepository;
import com.tomas.nutrition.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final MealRepository mealRepository;

    public UserService(UserRepository userRepository, MealRepository mealRepository) {
        this.userRepository = userRepository;
        this.mealRepository = mealRepository;
    }

    private void aplicarMetas(User user, UserRequestDTO dto) {
        if (dto.proteinaGPorKg() != null) {
            user.setProteinaGPorKg(dto.proteinaGPorKg());
        }
        if (dto.gorduraPercent() != null) {
            user.setGorduraPercent(dto.gorduraPercent());
        }
    }


    @Transactional
    public UserResponseDTO create(UserRequestDTO dto) {
        User user = new User(dto.nome(), dto.idade(), dto.peso(), dto.altura(),
                dto.genero(), dto.nivelAtividade());
        aplicarMetas(user, dto);
        return UserResponseDTO.from(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findById(Long id) {
        return UserResponseDTO.from(getUser(id));
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream()
                .map(UserResponseDTO::from)
                .toList();
    }

    @Transactional
    public UserResponseDTO update(Long id, UserRequestDTO dto) {
        User user = getUser(id);
        user.setNome(dto.nome());
        user.setIdade(dto.idade());
        user.setPeso(dto.peso());
        user.setAltura(dto.altura());
        user.setGenero(dto.genero());
        user.setNivelAtividade(dto.nivelAtividade());
        aplicarMetas(user, dto);
        return UserResponseDTO.from(user);
    }

    @Transactional
    public void delete(Long id) {
        User user = getUser(id);
        List<Meal> refeicoes = mealRepository.findByUserId(id);
        mealRepository.deleteAll(refeicoes);
        userRepository.delete(user);
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilizador", id));
    }
}