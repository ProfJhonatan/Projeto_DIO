package org.example.model;

import lombok.Getter;
import org.example.enums.StatusInvestimento;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa a carteira de investimentos de um cliente.
 * Agrupa todos os seus investimentos.
 */
@Getter
public class CarteiraInvestimento {
    private final List<Investimento> investimentos;

    public CarteiraInvestimento() {
        this.investimentos = new ArrayList<>();
    }

    public void adicionarInvestimento(Investimento investimento) {
        this.investimentos.add(investimento);
    }

    public double getSaldoTotal() {
        return investimentos.stream()
                .filter(inv -> inv.getStatus() == StatusInvestimento.ATIVO)
                .mapToDouble(Investimento::getValorAtual)
                .sum();
    }
}