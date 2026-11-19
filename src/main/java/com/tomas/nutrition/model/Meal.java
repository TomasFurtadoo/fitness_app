package com.tomas.nutrition.model;

import com.tomas.nutrition.exception.InvalidDataException;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meals")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private LocalDate data;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealItem> itens = new ArrayList<>();

    protected Meal() {
    }

    public Meal(String nome, User user, LocalDate data) {
        if (user == null) {
            throw new InvalidDataException("A refeicao tem de pertencer a um utilizador.");
        }
        setNome(nome);
        setData(data);
        this.user = user;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public LocalDate getData() { return data; }
    public User getUser() { return user; }
    public List<MealItem> getItens() { return itens; }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new InvalidDataException("O nome da refeicao nao pode ser vazio.");
        }
        this.nome = nome;
    }

    public void setData(LocalDate data) {
        if (data == null) {
            throw new InvalidDataException("A data da refeicao e obrigatoria.");
        }
        this.data = data;
    }

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