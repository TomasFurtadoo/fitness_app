package com.tomas.nutrition.model;

import com.tomas.nutrition.exception.InvalidDataException;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meals")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealItem> itens = new ArrayList<>();

    protected Meal() {
    }

    public Meal(String nome) {
        this.nome = nome;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public List<MealItem> getItens() { return itens; }

    public void addFood(Food alimento, double gramas) {
        if (gramas <= 0) {
            throw new InvalidDataException("A quantidade em gramas deve ser positiva.");
        }
        itens.add(new MealItem(this, alimento, gramas));
    }

    public void removeFood(Food alimento) {
        itens.removeIf(item -> item.getFood().equals(alimento));
    }

    public NutrientesPorcao getTotals() {
        NutrientesPorcao total = NutrientesPorcao.zero();
        for (MealItem item : itens) {
            total = total.somar(item.getNutrientes());
        }
        return total;
    }
}