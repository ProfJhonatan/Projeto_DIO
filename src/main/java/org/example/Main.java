package org.example;

import org.example.repository.ClienteRepository;
import org.example.service.BancoService;
import org.example.ui.MenuConsole;

public class Main {
    public static void main(String[] args) {
        // Injeção de Dependências com as novas classes
        ClienteRepository clienteRepository = new ClienteRepository();
        BancoService bancoService = new BancoService(clienteRepository);
        MenuConsole menu = new MenuConsole(bancoService);

        // Inicia a aplicação
        menu.exibirMenu();
    }
}