package com.tomas.nutrition.model;

import com.tomas.nutrition.exception.InvalidDataException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

class UserTest {

    private static User userMasculino() {
        return new User("Tomasio", 22, 75.0, 178.0, Genero.MASCULINO, NivelAtividade.MODERADAMENTE_ATIVO);
    }

    private static User userFeminino() {
        return new User("Ana", 30, 60.0, 165.0, Genero.FEMININO, NivelAtividade.SEDENTARIO);
    }

    // ---------- Calculos ----------

    @Test
    @DisplayName("TMB masculina (Mifflin-St Jeor) = 1757.5")
    void tmbMasculina() {
        // 10*75 + 6.25*178 - 5*22 + 5 = 750 + 1112.5 - 110 + 5
        assertThat(userMasculino().getTmb()).isCloseTo(1757.5, within(0.001));
    }

    @Test
    @DisplayName("TMB feminina usa s = -161")
    void tmbFeminina() {
        // 10*60 + 6.25*165 - 5*30 - 161 = 600 + 1031.25 - 150 - 161
        assertThat(userFeminino().getTmb()).isCloseTo(1320.25, within(0.001));
    }

    @Test
    @DisplayName("TDEE = TMB x fator de atividade")
    void tdee() {
        // 1757.5 * 1.55
        assertThat(userMasculino().getTdee()).isCloseTo(2724.125, within(0.001));
        // 1320.25 * 1.2
        assertThat(userFeminino().getTdee()).isCloseTo(1584.3, within(0.001));
    }

    @Test
    @DisplayName("IMC converte a altura de cm para metros")
    void imc() {
        // 75 / 1.78^2 = 23.67
        assertThat(userMasculino().getImc()).isCloseTo(23.67, within(0.01));
    }

    @ParameterizedTest(name = "peso {0}kg a 200cm -> {1}")
    @CsvSource({
            "73.6,  ABAIXO_DO_PESO",   // IMC 18.4
            "74.0,  PESO_NORMAL",      // IMC 18.5 (fronteira)
            "99.6,  PESO_NORMAL",      // IMC 24.9
            "100.0, SOBREPESO",        // IMC 25.0 (fronteira)
            "119.6, SOBREPESO",        // IMC 29.9
            "120.0, OBESIDADE"         // IMC 30.0 (fronteira)
    })
    @DisplayName("Classificacao do IMC respeita as fronteiras")
    void classificacaoImc(double peso, ClassificacaoIMC esperada) {
        User user = new User("Teste", 30, peso, 200.0, Genero.MASCULINO, NivelAtividade.SEDENTARIO);
        assertThat(user.getClassificacaoImc()).isEqualTo(esperada);
    }

    // ---------- Metas de macros ----------

    @Test
    @DisplayName("Metas: proteina = peso x g/kg e a soma das calorias das metas = TDEE")
    void metasSomamOTdee() {
        User user = userMasculino(); // 1.8 g/kg e 30% de gordura por defeito
        NutrientesPorcao metas = user.getMetas();

        assertThat(metas.calorias()).isCloseTo(2724.125, within(0.001));
        assertThat(metas.proteina()).isCloseTo(135.0, within(0.001)); // 75 * 1.8

        double kcalDasMetas = metas.proteina() * 4 + metas.carbs() * 4 + metas.gordura() * 9;
        assertThat(kcalDasMetas).isCloseTo(user.getTdee(), within(0.001));
    }

    @Test
    @DisplayName("Metas: carboidratos nunca ficam negativos")
    void carbsNuncaNegativos() {
        User user = new User("Ana", 60, 100.0, 150.0, Genero.FEMININO, NivelAtividade.SEDENTARIO);
        user.setProteinaGPorKg(3.0);
        user.setGorduraPercent(50);

        // proteina (1200 kcal) + gordura (~886 kcal) ultrapassam o TDEE (~1772 kcal)
        assertThat(user.getMetas().carbs()).isEqualTo(0.0);
    }

    // ---------- Validacoes: idade ----------

    @ParameterizedTest
    @ValueSource(ints = {0, -5, 121})
    @DisplayName("Idade fora de 1-120 e rejeitada")
    void idadeInvalida(int idade) {
        assertThatThrownBy(() -> userMasculino().setIdade(idade))
                .isInstanceOf(InvalidDataException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 120})
    @DisplayName("Idade nos limites 1 e 120 e aceite")
    void idadeLimitesValidos(int idade) {
        assertThatCode(() -> userMasculino().setIdade(idade)).doesNotThrowAnyException();
    }

    // ---------- Validacoes: peso ----------

    @ParameterizedTest
    @ValueSource(doubles = {20.0, 0.0, -1.0})
    @DisplayName("Peso igual ou inferior a 20kg e rejeitado")
    void pesoInvalido(double peso) {
        assertThatThrownBy(() -> userMasculino().setPeso(peso))
                .isInstanceOf(InvalidDataException.class);
    }

    @Test
    @DisplayName("Peso logo acima de 20kg e aceite")
    void pesoLimiteValido() {
        assertThatCode(() -> userMasculino().setPeso(20.1)).doesNotThrowAnyException();
    }

    // ---------- Validacoes: altura ----------

    @ParameterizedTest
    @ValueSource(doubles = {49.9, 251.0})
    @DisplayName("Altura fora de 50-250cm e rejeitada")
    void alturaInvalida(double altura) {
        assertThatThrownBy(() -> userMasculino().setAltura(altura))
                .isInstanceOf(InvalidDataException.class);
    }

    @ParameterizedTest
    @ValueSource(doubles = {50.0, 250.0})
    @DisplayName("Altura nos limites 50 e 250 e aceite")
    void alturaLimitesValidos(double altura) {
        assertThatCode(() -> userMasculino().setAltura(altura)).doesNotThrowAnyException();
    }

    // ---------- Validacoes: nome, construtor e preferencias ----------

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Nome vazio, nulo ou so com espacos e rejeitado")
    void nomeInvalido(String nome) {
        assertThatThrownBy(() -> userMasculino().setNome(nome))
                .isInstanceOf(InvalidDataException.class);
    }

    @Test
    @DisplayName("O construtor tambem valida (usa os setters)")
    void construtorValida() {
        assertThatThrownBy(() ->
                new User("X", 150, 75.0, 178.0, Genero.MASCULINO, NivelAtividade.SEDENTARIO))
                .isInstanceOf(InvalidDataException.class);
    }

    @Test
    @DisplayName("Preferencias de macros: valores por defeito e limites")
    void preferenciasMacros() {
        User user = userMasculino();
        assertThat(user.getProteinaGPorKg()).isEqualTo(1.8);
        assertThat(user.getGorduraPercent()).isEqualTo(30.0);

        assertThatThrownBy(() -> user.setProteinaGPorKg(0.7)).isInstanceOf(InvalidDataException.class);
        assertThatThrownBy(() -> user.setProteinaGPorKg(3.1)).isInstanceOf(InvalidDataException.class);
        assertThatThrownBy(() -> user.setGorduraPercent(14)).isInstanceOf(InvalidDataException.class);
        assertThatThrownBy(() -> user.setGorduraPercent(51)).isInstanceOf(InvalidDataException.class);
    }
}