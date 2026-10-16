package com.tomas.nutrition.model;

public enum ClassificacaoIMC {
    ABAIXO_DO_PESO("Abaixo do peso"),
    PESO_NORMAL("Peso normal"),
    SOBREPESO("Sobrepeso"),
    OBESIDADE("Obesidade");

    private final String descricao;

    ClassificacaoIMC(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}