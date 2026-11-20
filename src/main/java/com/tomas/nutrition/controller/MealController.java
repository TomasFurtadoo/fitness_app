package com.tomas.nutrition.controller;

import com.tomas.nutrition.dto.MealItemRequestDTO;
import com.tomas.nutrition.dto.MealRequestDTO;
import com.tomas.nutrition.dto.MealResponseDTO;
import com.tomas.nutrition.service.MealService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/meals")
public class MealController {

    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @PostMapping
    public ResponseEntity<MealResponseDTO> create(@Valid @RequestBody MealRequestDTO dto) {
        MealResponseDTO criada = mealService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criada.id()).toUri();
        return ResponseEntity.created(location).body(criada);
    }

    /** GET /api/meals?userId=1  ou  GET /api/meals?userId=1&data=2026-10-15 */
    @GetMapping
    public List<MealResponseDTO> findByUser(
            @RequestParam Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return mealService.findByUser(userId, data);
    }

    @GetMapping("/{id}")
    public MealResponseDTO findById(@PathVariable Long id) {
        return mealService.findById(id);
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<MealResponseDTO> addItem(@PathVariable Long id,
                                                   @Valid @RequestBody MealItemRequestDTO dto) {
        return ResponseEntity.status(201).body(mealService.addItem(id, dto));
    }

    @DeleteMapping("/{id}/items/{foodId}")
    public MealResponseDTO removeItem(@PathVariable Long id, @PathVariable Long foodId) {
        return mealService.removeItem(id, foodId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        mealService.delete(id);
        return ResponseEntity.noContent().build();
    }
}