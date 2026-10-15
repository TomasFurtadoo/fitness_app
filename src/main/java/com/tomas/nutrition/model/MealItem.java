package com.tomas.nutrition.model;

import com.tomas.nutrition.exception.InvalidDataException;
import jakarta.persistence.*;

@Entity
@Table(name = "meal_items")
public class MealItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "meal_id")
    private Meal meal;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;

    private double gramas;

    protected MealItem() {
    }

    public MealItem(Meal meal, Food food, double gramas) {
        setGramas(gramas);
        this.meal = meal;
        this.food = food;
    }

    public Long getId() { return id; }
    public Meal getMeal() { return meal; }
    public Food getFood() { return food; }
    public double getGramas() { return gramas; }

    public void setGramas(double gramas) {
        if (gramas <= 0) {
            throw new InvalidDataException("A quantidade em gramas deve ser positiva.");
        }
        this.gramas = gramas;
    }

    public NutrientesPorcao getNutrientes() {
        return food.getNutrientsByPortion(gramas);
    }
}