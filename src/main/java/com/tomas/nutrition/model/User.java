package com.tomas.nutrition.model;

import com.tomas.nutrition.exception.InvalidDataException;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private int idade;
    private double peso;      // kg
    private double altura;    // cm

    @Enumerated(EnumType.STRING)
    private Genero genero;

    @Enumerated(EnumType.STRING)
    private NivelAtividade nivelAtividade;

    protected User() {
    }

    public User(String nome, int idade, double peso, double altura,
                Genero genero, NivelAtividade nivelAtividade) {
        setNome(nome);
        setIdade(idade);
        setPeso(peso);
        setAltura(altura);
        this.genero = genero;
        this.nivelAtividade = nivelAtividade;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public int getIdade() { return idade; }
    public double getPeso() { return peso; }
    public double getAltura() { return altura; }
    public Genero getGenero() { return genero; }
    public NivelAtividade getNivelAtividade() { return nivelAtividade; }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new InvalidDataException("O nome nao pode ser vazio.");
        }
        this.nome = nome;
    }

    public void setIdade(int idade) {
        if (idade < 1 || idade > 120) {
            throw new InvalidDataException("A idade deve estar entre 1 e 120 anos.");
        }
        this.idade = idade;
    }

    public void setPeso(double peso) {
        if (peso <= 20) {
            throw new InvalidDataException("O peso deve ser superior a 20kg.");
        }
        this.peso = peso;
    }

    public void setAltura(double altura) {
        if (altura < 50 || altura > 250) {
            throw new InvalidDataException("A altura deve estar entre 50 e 250cm.");
        }
        this.altura = altura;
    }

    public void setGenero(Genero genero) { this.genero = genero; }
    public void setNivelAtividade(NivelAtividade nivelAtividade) { this.nivelAtividade = nivelAtividade; }

    public double getImc() {
        double alturaMetros = altura / 100.0;
        return peso / (alturaMetros * alturaMetros);
    }

    public ClassificacaoIMC getClassificacaoImc() {
        double imc = getImc();
        if (imc < 18.5) {
            return ClassificacaoIMC.ABAIXO_DO_PESO;
        } else if (imc < 25) {
            return ClassificacaoIMC.PESO_NORMAL;
        } else if (imc < 30) {
            return ClassificacaoIMC.SOBREPESO;
        } else {
            return ClassificacaoIMC.OBESIDADE;
        }
    }

    public double getTmb() {
        double s = (genero == Genero.MASCULINO) ? 5 : -161;
        return (10 * peso) + (6.25 * altura) - (5 * idade) + s;
    }

    public double getTdee() {
        return getTmb() * nivelAtividade.getFator();
    }
}