package com.tomas.nutrition.repository;

import com.tomas.nutrition.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

    List<Food> findByNomeContainingIgnoreCase(String nome);
}