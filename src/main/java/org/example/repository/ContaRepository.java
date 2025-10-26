package org.example.repository;

import org.example.model.Conta;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Repositório para simular a persistência de contas em memória.
 * Utiliza um Map para armazenar as contas, onde a chave é o número da conta.
 */
public class ContaRepository {
    // O Map é privado e final para garantir que a instância não seja trocada.
    private final Map<String, Conta> contas = new HashMap<>();

    /**
     * Salva ou atualiza uma conta no repositório.
     * @param conta A conta a ser salva.
     */
    public void salvar(Conta conta) {
        contas.put(conta.getNumero(), conta);
    }

    /**
     * Busca uma conta pelo seu número.
     * @param numero O número da conta a ser buscada.
     * @return um Optional contendo a conta se encontrada, ou um Optional vazio caso contrário.
     * O uso de Optional evita NullPointerExceptions.
     */
    public Optional<Conta> buscarPorNumero(String numero) {
        return Optional.ofNullable(contas.get(numero));
    }
}