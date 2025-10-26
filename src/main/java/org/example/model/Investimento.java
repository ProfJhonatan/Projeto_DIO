package org.example.model;

import lombok.Getter;
import lombok.Setter;
import org.example.enums.StatusInvestimento;

import java.time.LocalDate;

@Getter
public class Investimento {
    private final String nome;
    private final double valorAplicado;
    private final LocalDate dataAplicacao;

    // Campos adicionados para permitir atualização e resgate.
    @Setter
    private double valorAtual;
    @Setter
    private StatusInvestimento status;

    public Investimento(String nome, double valorAplicado) {
        this.nome = nome;
        this.valorAplicado = valorAplicado;
        this.dataAplicacao = LocalDate.now();
        this.valorAtual = valorAplicado; // O valor atual começa igual ao aplicado.
        this.status = StatusInvestimento.ATIVO;
    }

    @Override
    public String toString() {
        return String.format("Investimento: %s | Aplicado: R$%.2f | Valor Atual: R$%.2f | Status: %s | Data: %s",
                nome, valorAplicado, valorAtual, status, dataAplicacao);
    }
}