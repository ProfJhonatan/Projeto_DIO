package org.example.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe central que representa um cliente.
 * Agora possui uma lista de contas e uma carteira de investimentos.
 */
@Getter
public class Cliente {
    private final String nome;
    private final String cpf;
    private final List<Conta> contas;
    private final CarteiraInvestimento carteiraInvestimento;

    public Cliente(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
        this.contas = new ArrayList<>();
        this.carteiraInvestimento = new CarteiraInvestimento();
    }

    public void adicionarConta(Conta conta) {
        this.contas.add(conta);
    }
}
