package com.tomas.nutrition.controller;

import com.tomas.nutrition.dto.FoodRequestDTO;
import com.tomas.nutrition.dto.FoodResponseDTO;
import com.tomas.nutrition.service.FoodService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/foods")
public class FoodController {

    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @PostMapping
    public ResponseEntity<FoodResponseDTO> create(@Valid @RequestBody FoodRequestDTO dto) {
        FoodResponseDTO criado = foodService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(location).body(criado);
    }

    /** GET /api/foods ou GET /api/foods?nome=arr (pesquisa parcial, sem distinguir maiusculas). */
    @GetMapping
    public List<FoodResponseDTO> findAll(@RequestParam(required = false) String nome) {
        if (nome != null && !nome.isBlank()) {
            return foodService.searchByNome(nome);
        }
        return foodService.findAll();
    }

    @GetMapping("/{id}")
    public FoodResponseDTO findById(@PathVariable Long id) {
        return foodService.findById(id);
    }

    @PutMapping("/{id}")
    public FoodResponseDTO update(@PathVariable Long id, @Valid @RequestBody FoodRequestDTO dto) {
        return foodService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        foodService.delete(id);
        return ResponseEntity.noContent().build();
    }
}