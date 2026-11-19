package com.tomas.nutrition.repository;

import com.tomas.nutrition.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    List<Meal> findByUserIdAndData(Long userId, LocalDate data);

    List<Meal> findByUserId(Long userId);
}