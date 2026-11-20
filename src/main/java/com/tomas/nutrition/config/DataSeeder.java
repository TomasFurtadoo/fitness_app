package com.tomas.nutrition.config;

import com.tomas.nutrition.model.Food;
import com.tomas.nutrition.repository.FoodRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final FoodRepository foodRepository;

    public DataSeeder(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    @Override
    public void run(String... args) {
        if (foodRepository.count() > 0) {
            return;
        }
        // Valores aproximados por 100g (tabelas nutricionais tipo USDA). Valida-os antes de os usares a serio.
        foodRepository.saveAll(List.of(
                new Food("Arroz cozido", 130, 2.7, 28.0, 0.3),
                new Food("Peito de frango cozido", 165, 31.0, 0.0, 3.6),
                new Food("Ovo cozido", 143, 12.6, 0.7, 9.5),
                new Food("Banana", 89, 1.1, 22.8, 0.3),
                new Food("Aveia", 389, 16.9, 66.3, 6.9),
                new Food("Azeite", 884, 0.0, 0.0, 100.0),
                new Food("Salmao cozido", 208, 20.0, 0.0, 13.0),
                new Food("Brocolos cozidos", 35, 2.4, 7.2, 0.4),
                new Food("Leite meio-gordo", 46, 3.3, 4.8, 1.6),
                new Food("Batata cozida", 87, 1.9, 20.1, 0.1),
                new Food("Amendoas", 579, 21.2, 21.6, 49.9)
        ));
    }
}