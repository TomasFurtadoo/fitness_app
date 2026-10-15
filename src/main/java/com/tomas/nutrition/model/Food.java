package com.tomas.nutrition.model;

import com.tomas.nutrition.exception.InvalidDataException;
import jakarta.persistence.*;

@Entity
@Table(name = "foods")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private double calorias100g;
    private double proteina100g;
    private double carbs100g;
    private double gordura100g;

    protected Food() {
    }

    public Food(String nome, double calorias100g, double proteina100g,
                double carbs100g, double gordura100g) {
        setNome(nome);
        setCalorias100g(calorias100g);
        setProteina100g(proteina100g);
        setCarbs100g(carbs100g);
        setGordura100g(gordura100g);
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public double getCalorias100g() { return calorias100g; }
    public double getProteina100g() { return proteina100g; }
    public double getCarbs100g() { return carbs100g; }
    public double getGordura100g() { return gordura100g; }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new InvalidDataException("O nome do alimento nao pode ser vazio.");
        }
        this.nome = nome;
    }

    public void setCalorias100g(double calorias100g) {
        if (calorias100g < 0) {
            throw new InvalidDataException("As calorias nao podem ser negativas.");
        }
        this.calorias100g = calorias100g;
    }

    public void setProteina100g(double proteina100g) {
        if (proteina100g < 0) {
            throw new InvalidDataException("A proteina nao pode ser negativa.");
        }
        this.proteina100g = proteina100g;
    }

    public void setCarbs100g(double carbs100g) {
        if (carbs100g < 0) {
            throw new InvalidDataException("Os carboidratos nao podem ser negativos.");
        }
        this.carbs100g = carbs100g;
    }

    public void setGordura100g(double gordura100g) {
        if (gordura100g < 0) {
            throw new InvalidDataException("A gordura nao pode ser negativa.");
        }
        this.gordura100g = gordura100g;
    }

    public NutrientesPorcao getNutrientsByPortion(double gramas) {
        if (gramas <= 0) {
            throw new InvalidDataException("A quantidade em gramas deve ser positiva.");
        }
        double fator = gramas / 100.0;
        return new NutrientesPorcao(
                calorias100g * fator,
                proteina100g * fator,
                carbs100g * fator,
                gordura100g * fator
        );
    }
}