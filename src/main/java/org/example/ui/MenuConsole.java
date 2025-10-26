package org.example.ui;

import org.example.model.Cliente;
import org.example.model.Conta;
import org.example.model.ContaCorrente;
import org.example.service.BancoService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Camada de Interface do Usuário (UI) baseada em console.
 * Responsável por toda a interação com o usuário final, como exibir menus,
 * capturar entradas e apresentar os resultados das operações.
 * Delega todas as ações de negócio para a classe BancoService.
 */
public class MenuConsole {
    private final BancoService bancoService;
    private final Scanner scanner;

    /**
     * Construtor do Menu. Recebe o serviço como dependência.
     * @param bancoService A instância do serviço que contém a lógica de negócio.
     */
    public MenuConsole(BancoService bancoService) {
        this.bancoService = bancoService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Exibe o menu principal e gerencia o fluxo de navegação do usuário.
     * Continua em loop até que o usuário escolha a opção de sair (0).
     */
    public void exibirMenu() {
        int opcao;
        do {
            System.out.println("\n--- BANCO DIGITAL - MENU PRINCIPAL ---");
            System.out.println("1. Criar Cliente (Contato)");
            System.out.println("2. Adicionar Conta para Cliente (Corrente ou Poupança)");
            System.out.println("3. Depositar");
            System.out.println("4. Sacar");
            System.out.println("5. Transferência entre Contas");
            System.out.println("6. Histórico da Conta");
            System.out.println("7. Listar Clientes (Contatos)");
            System.out.println("8. Menu de Investimentos");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = lerInt();

            switch (opcao) {
                case 1 -> criarCliente();
                case 2 -> adicionarConta();
                case 3 -> depositar();
                case 4 -> sacar();
                case 5 -> transferir();
                case 6 -> verHistorico();
                case 7 -> bancoService.listarClientes();
                case 8 -> menuInvestimentos();
                case 0 -> System.out.println("\nObrigado por usar nossos serviços!");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    /**
     * Exibe um submenu focado apenas nas funcionalidades de investimento.
     */
    private void menuInvestimentos() {
        int opcao;
        do {
            System.out.println("\n--- MENU DE INVESTIMENTOS ---");
            System.out.println("1. Fazer Investimento");
            System.out.println("2. Listar Carteira de Investimentos de um Cliente");
            System.out.println("3. Resgatar (Sacar) Investimento");
            System.out.println("4. Simular Atualização de Rendimentos (para todos)");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");
            opcao = lerInt();

            switch (opcao) {
                case 1 -> fazerInvestimento();
                case 2 -> listarCarteira();
                case 3 -> resgatarInvestimento();
                case 4 -> bancoService.simularAtualizacaoInvestimentos();
                case 0 -> System.out.println("Retornando ao menu principal...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    // --- MÉTODOS PRIVADOS PARA CADA OPÇÃO DO MENU ---
    // Cada método abaixo é responsável por coletar os dados do usuário
    // para uma operação específica e chamar o método correspondente no BancoService.

    private void criarCliente() {
        System.out.println("\n>> 1. CRIAR CLIENTE");
        System.out.print("Nome do cliente: ");
        String nome = scanner.nextLine();
        System.out.print("CPF do cliente: ");
        String cpf = scanner.nextLine();
        Cliente c = bancoService.criarCliente(nome, cpf);
        if(c != null) System.out.println("Cliente criado com sucesso!");
    }

    private void adicionarConta() {
        System.out.println("\n>> 2. ADICIONAR CONTA");
        System.out.print("CPF do cliente: ");
        String cpf = scanner.nextLine();
        System.out.print("Tipo da conta (corrente/poupanca): ");
        String tipo = scanner.nextLine();
        Conta conta = bancoService.adicionarContaParaCliente(cpf, tipo);
        if(conta != null) {
            System.out.println("Conta criada com sucesso!");
            System.out.printf("Agência: %s, Conta: %s%n", conta.getAgencia(), conta.getNumero());
        }
    }

    private void depositar() {
        System.out.println("\n>> 3. DEPOSITAR");
        System.out.print("CPF do titular da conta: ");
        String cpf = scanner.nextLine();
        Conta contaSelecionada = selecionarContaDoCliente(cpf, "para depósito");

        if (contaSelecionada != null) {
            System.out.print("Valor a depositar: ");
            double valor = lerDouble();
            String feedback = bancoService.depositar(contaSelecionada, valor);
            System.out.println(feedback);
        }
    }

    private void sacar() {
        System.out.println("\n>> 4. SACAR");
        System.out.print("CPF do titular da conta: ");
        String cpf = scanner.nextLine();
        Conta contaSelecionada = selecionarContaDoCliente(cpf, "para saque");

        if (contaSelecionada != null) {
            System.out.print("Valor a sacar: ");
            double valor = lerDouble();
            String feedback = bancoService.sacar(contaSelecionada, valor);
            System.out.println(feedback);
        }
    }

    private void transferir() {
        System.out.println("\n>> 5. TRANSFERIR");
        System.out.print("Número da conta de origem: ");
        String origem = scanner.nextLine();
        System.out.print("Número da conta de destino: ");
        String destino = scanner.nextLine();
        System.out.print("Valor a transferir: ");
        double valor = lerDouble();
        String feedback = bancoService.transferir(origem, destino, valor);
        System.out.println(feedback);
    }

    private void verHistorico() {
        System.out.println("\n>> 6. VER HISTÓRICO");
        System.out.print("Digite o número da conta para ver o histórico: ");
        String numConta = scanner.nextLine();
        bancoService.verHistorico(numConta);
    }

    private void fazerInvestimento() {
        System.out.println("\n>> INVESTIMENTO: APLICAR");
        System.out.print("CPF do cliente: ");
        String cpf = scanner.nextLine();
        Conta contaSelecionada = selecionarContaDoCliente(cpf, "de onde sairá o valor");

        if(contaSelecionada != null) {
            System.out.print("Nome do investimento (Ex: CDB, Ações XYZ): ");
            String nomeInv = scanner.nextLine();
            System.out.print("Valor a investir: ");
            double valor = lerDouble();
            String feedback = bancoService.fazerInvestimento(contaSelecionada, nomeInv, valor);
            System.out.println(feedback);
        }
    }

    private void listarCarteira() {
        System.out.println("\n>> INVESTIMENTO: LISTAR CARTEIRA");
        System.out.print("CPF do cliente: ");
        String cpf = scanner.nextLine();
        bancoService.listarCarteira(cpf);
    }

    private void resgatarInvestimento() {
        System.out.println("\n>> INVESTIMENTO: RESGATAR");
        System.out.print("CPF do cliente: ");
        String cpf = scanner.nextLine();
        Optional<Cliente> clienteOpt = bancoService.buscarClientePorCpf(cpf);

        if (clienteOpt.isPresent()) {
            bancoService.listarCarteira(cpf);
            if(clienteOpt.get().getCarteiraInvestimento().getInvestimentos().isEmpty()) return;

            System.out.print("Digite o índice do investimento a resgatar: ");
            int indice = lerInt();

            Conta contaDestino = selecionarContaDoCliente(cpf, "para onde o valor será creditado");
            if (contaDestino != null) {
                String feedback = bancoService.resgatarInvestimento(clienteOpt.get(), indice, contaDestino);
                System.out.println(feedback);
            }
        } else {
            System.out.println("Cliente não encontrado.");
        }
    }

    // --- MÉTODOS UTILITÁRIOS ---

    /**
     * Método auxiliar para permitir que o usuário escolha uma conta quando um cliente
     * possui mais de uma (Corrente e Poupança).
     * @param cpf O CPF do cliente.
     * @param motivo Uma string para contextualizar a seleção (ex: "para saque").
     * @return A Conta escolhida pelo usuário, ou null se a seleção falhar.
     */
    private Conta selecionarContaDoCliente(String cpf, String motivo) {
        Optional<Cliente> clienteOpt = bancoService.buscarClientePorCpf(cpf);
        if (clienteOpt.isEmpty()) {
            System.out.println("Cliente não encontrado.");
            return null;
        }

        List<Conta> contas = clienteOpt.get().getContas();
        if (contas.isEmpty()) {
            System.out.println("Este cliente não possui contas.");
            return null;
        }

        if (contas.size() == 1) {
            return contas.get(0);
        } else {
            System.out.printf("Este cliente possui mais de uma conta. Selecione a conta %s:%n", motivo);
            for (int i = 0; i < contas.size(); i++) {
                Conta c = contas.get(i);
                String tipo = c instanceof ContaCorrente ? "Corrente" : "Poupança";
                System.out.printf("[%d] - %s (Conta: %s)%n", i, tipo, c.getNumero());
            }
            System.out.print("Digite o índice da conta: ");
            int indice = lerInt();
            if (indice >= 0 && indice < contas.size()) {
                return contas.get(indice);
            } else {
                System.out.println("Índice inválido.");
                return null;
            }
        }
    }

    private Conta selecionarContaDoCliente(String cpf) {
        return selecionarContaDoCliente(cpf, "");
    }

    /**
     * Lê um número inteiro do console, tratando entradas inválidas.
     * @return O número inteiro lido.
     */
    private int lerInt() {
        while (!scanner.hasNextInt()) {
            System.out.print("Entrada inválida. Por favor, digite um número inteiro: ");
            scanner.next();
        }
        int valor = scanner.nextInt();
        scanner.nextLine(); // Limpa o buffer do scanner
        return valor;
    }

    /**
     * Lê um número de ponto flutuante (double) do console, tratando entradas inválidas.
     * @return O número double lido.
     */
    private double lerDouble() {
        while (!scanner.hasNextDouble()) {
            System.out.print("Entrada inválida. Por favor, digite um valor numérico (ex: 100,50): ");
            scanner.next();
        }
        double valor = scanner.nextDouble();
        scanner.nextLine(); // Limpa o buffer do scanner
        return valor;
    }
}