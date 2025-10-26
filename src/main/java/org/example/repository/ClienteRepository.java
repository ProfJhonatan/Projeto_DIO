package org.example.repository;

import org.example.model.Cliente;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Repositório para gerenciar a persistência de Clientes em memória.
 * A chave do Map é o CPF do cliente.
 */
public class ClienteRepository {
    private final Map<String, Cliente> clientes = new HashMap<>();

    public void salvar(Cliente cliente) {
        clientes.put(cliente.getCpf(), cliente);
    }

    public Optional<Cliente> buscarPorCpf(String cpf) {
        return Optional.ofNullable(clientes.get(cpf));
    }

    public Collection<Cliente> buscarTodos() {
        return clientes.values();
    }
}
