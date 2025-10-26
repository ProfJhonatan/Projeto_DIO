package org.example.service;

import org.example.enums.StatusInvestimento;
import org.example.model.*;
import org.example.repository.ClienteRepository;

import java.util.Optional;

/**
 * Camada de Serviço (Service Layer) que centraliza todas as regras de negócio do banco.
 * Esta classe atua como um intermediário entre a interface do usuário (MenuConsole) e
 * a camada de dados (ClienteRepository), garantindo que as operações sejam executadas
 * de forma correta e segura.
 */
public class BancoService {

    private final ClienteRepository clienteRepository;

    /**
     * Construtor do serviço. Recebe o repositório como uma dependência (Injeção de Dependência).
     * @param clienteRepository O repositório que gerencia os dados dos clientes.
     */
    public BancoService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // --- MÉTODOS DE CLIENTE E CONTA ---

    /**
     * Cria um novo cliente e o salva no repositório.
     * @param nome O nome completo do cliente.
     * @param cpf O CPF do cliente (usado como identificador único).
     * @return O objeto Cliente recém-criado, ou null se o CPF já existir.
     */
    public Cliente criarCliente(String nome, String cpf) {
        if (clienteRepository.buscarPorCpf(cpf).isPresent()) {
            System.out.println("Erro: CPF já cadastrado.");
            return null;
        }
        Cliente cliente = new Cliente(nome, cpf);
        clienteRepository.salvar(cliente);
        return cliente;
    }

    /**
     * Adiciona uma nova conta (Corrente ou Poupança) a um cliente já existente.
     * @param cpf O CPF do cliente ao qual a conta será adicionada.
     * @param tipoConta O tipo da conta, "corrente" ou "poupanca".
     * @return O objeto Conta recém-criado, ou null se ocorrer um erro.
     */
    public Conta adicionarContaParaCliente(String cpf, String tipoConta) {
        Optional<Cliente> clienteOpt = clienteRepository.buscarPorCpf(cpf);
        if (clienteOpt.isEmpty()) {
            System.out.println("Erro: Cliente não encontrado.");
            return null;
        }
        Cliente cliente = clienteOpt.get();
        boolean tipoJaExiste = cliente.getContas().stream().anyMatch(c ->
                ("corrente".equalsIgnoreCase(tipoConta) && c instanceof ContaCorrente) ||
                        ("poupanca".equalsIgnoreCase(tipoConta) && c instanceof ContaPoupanca)
        );
        if(tipoJaExiste){
            System.out.println("Erro: O cliente já possui uma conta deste tipo.");
            return null;
        }

        Conta novaConta = "corrente".equalsIgnoreCase(tipoConta) ? new ContaCorrente(cliente) : new ContaPoupanca(cliente);
        cliente.adicionarConta(novaConta);
        return novaConta;
    }

    /**
     * Busca um cliente pelo seu CPF.
     * @param cpf O CPF a ser pesquisado.
     * @return Um Optional contendo o Cliente, se encontrado.
     */
    public Optional<Cliente> buscarClientePorCpf(String cpf) {
        return clienteRepository.buscarPorCpf(cpf);
    }

    // --- OPERAÇÕES BANCÁRIAS COM FEEDBACK DETALHADO ---

    /**
     * Realiza um depósito em uma conta específica.
     * @param conta O objeto Conta que receberá o depósito.
     * @param valor O montante a ser depositado.
     * @return Uma string com a mensagem de sucesso e o novo saldo.
     */
    public String depositar(Conta conta, double valor) {
        if (conta == null) return "Erro: Conta inválida.";
        conta.depositar(valor);
        return String.format("Depósito de R$%.2f realizado com sucesso. Novo saldo: R$%.2f", valor, conta.getSaldo());
    }

    /**
     * Realiza um saque de uma conta específica.
     * @param conta O objeto Conta de onde o dinheiro será sacado.
     * @param valor O montante a ser sacado.
     * @return Uma string com a mensagem de sucesso e o novo saldo, ou uma mensagem de erro.
     */
    public String sacar(Conta conta, double valor) {
        if (conta == null) return "Erro: Conta inválida.";
        if (conta.sacar(valor)) {
            return String.format("Saque de R$%.2f realizado com sucesso. Novo saldo: R$%.2f", valor, conta.getSaldo());
        }
        return "Saque não realizado. Verifique o saldo ou o valor solicitado.";
    }

    /**
     * Realiza uma transferência entre duas contas.
     * @param numContaOrigem O número da conta que enviará o dinheiro.
     * @param numContaDestino O número da conta que receberá o dinheiro.
     * @param valor O montante a ser transferido.
     * @return Uma string de feedback detalhando a operação.
     */
    public String transferir(String numContaOrigem, String numContaDestino, double valor) {
        Optional<Conta> origemOpt = findContaByNumero(numContaOrigem);
        Optional<Conta> destinoOpt = findContaByNumero(numContaDestino);

        if (origemOpt.isPresent() && destinoOpt.isPresent()) {
            Conta origem = origemOpt.get();
            if (origem.sacar(valor)) {
                destinoOpt.get().depositar(valor);
                return String.format("Transferência de R$%.2f realizada da conta %s para a conta %s.\nSaldo atual da conta de origem: R$%.2f", valor, numContaOrigem, numContaDestino, origem.getSaldo());
            } else {
                return "Transferência não realizada. Saldo insuficiente na conta de origem.";
            }
        }
        return "Erro: Conta de origem ou destino não encontrada.";
    }

    // --- OPERAÇÕES DE INVESTIMENTO COM FEEDBACK DETALHADO ---

    /**
     * Cria uma nova aplicação de investimento, debitando o valor de uma conta.
     * @param conta A conta da qual o valor será debitado.
     * @param nomeInvestimento O nome para identificar o investimento (ex: "Ações XPTO").
     * @param valor O valor a ser investido.
     * @return Uma string de feedback detalhando a operação.
     */
    public String fazerInvestimento(Conta conta, String nomeInvestimento, double valor) {
        if (conta == null) return "Erro: Conta de débito inválida.";
        if (conta.sacar(valor)) {
            Investimento inv = new Investimento(nomeInvestimento, valor);
            conta.getCliente().getCarteiraInvestimento().adicionarInvestimento(inv);
            return String.format("Investimento em '%s' no valor de R$%.2f realizado com sucesso.\nSaldo atual da conta: R$%.2f", nomeInvestimento, valor, conta.getSaldo());
        }
        return "Não foi possível realizar o investimento. Saldo insuficiente.";
    }

    /**
     * Resgata um investimento, movendo seu valor atual para uma conta.
     * @param cliente O cliente dono do investimento.
     * @param indice O índice do investimento na lista da carteira.
     * @param contaDestino A conta que receberá o valor resgatado.
     * @return Uma string de feedback detalhando o resgate.
     */
    public String resgatarInvestimento(Cliente cliente, int indice, Conta contaDestino) {
        CarteiraInvestimento carteira = cliente.getCarteiraInvestimento();
        if (indice < 0 || indice >= carteira.getInvestimentos().size()) return "Índice de investimento inválido.";
        Investimento inv = carteira.getInvestimentos().get(indice);
        if (inv.getStatus() == StatusInvestimento.RESGATADO) return "Este investimento já foi resgatado.";

        double valorResgate = inv.getValorAtual();
        contaDestino.depositar(valorResgate);
        inv.setStatus(StatusInvestimento.RESGATADO);

        return String.format("Investimento resgatado. Valor de R$%.2f creditado na conta %s.\nNovo saldo da conta: R$%.2f", valorResgate, contaDestino.getNumero(), contaDestino.getSaldo());
    }

    // --- MÉTODOS DE CONSULTA ---

    /**
     * Imprime no console uma lista de todos os clientes cadastrados e suas respectivas contas.
     */
    public void listarClientes() {
        System.out.println("\n--- LISTA DE CLIENTES E CONTAS ---");
        clienteRepository.buscarTodos().forEach(cliente -> {
            System.out.println("----------------------------------------");
            System.out.printf("Cliente: %s | CPF: %s%n", cliente.getNome(), cliente.getCpf());
            if(cliente.getContas().isEmpty()){
                System.out.println("  (Nenhuma conta cadastrada)");
            } else {
                cliente.getContas().forEach(conta -> {
                    String tipo = conta instanceof ContaCorrente ? "Conta Corrente" : "Conta Poupança";
                    System.out.printf("  - Tipo: %-15s | Ag: %s | Conta: %s | Saldo: R$%.2f%n", tipo, conta.getAgencia(), conta.getNumero(), conta.getSaldo());
                });
            }
        });
        System.out.println("----------------------------------------");
    }

    /**
     * Imprime no console a carteira de investimentos de um cliente específico.
     * @param cpf O CPF do cliente a ser consultado.
     */
    public void listarCarteira(String cpf) {
        clienteRepository.buscarPorCpf(cpf).ifPresentOrElse(cliente -> {
            System.out.printf("\n--- CARTEIRA DE INVESTIMENTOS DE %s ---%n", cliente.getNome());
            CarteiraInvestimento carteira = cliente.getCarteiraInvestimento();
            if (carteira.getInvestimentos().isEmpty()) {
                System.out.println("Nenhum investimento na carteira.");
            } else {
                for (int i = 0; i < carteira.getInvestimentos().size(); i++) {
                    System.out.printf("[%d] %s%n", i, carteira.getInvestimentos().get(i));
                }
            }
            System.out.printf(">> Saldo Total Investido (Ativos): R$%.2f%n", carteira.getSaldoTotal());
        }, () -> System.out.println("Cliente não encontrado."));
    }

    /**
     * Simula um rendimento aleatório para todos os investimentos ativos de todos os clientes.
     */
    public void simularAtualizacaoInvestimentos() {
        clienteRepository.buscarTodos().forEach(cliente -> cliente.getCarteiraInvestimento().getInvestimentos().forEach(inv -> {
            if (inv.getStatus() == StatusInvestimento.ATIVO) {
                double rendimento = 1 + (Math.random() * 0.05);
                inv.setValorAtual(inv.getValorAtual() * rendimento);
            }
        }));
        System.out.println("Simulação de rendimentos concluída para todos os investimentos ativos.");
    }

    /**
     * Busca uma conta pelo número e imprime seu extrato detalhado.
     * @param numeroConta O número da conta a ser consultada.
     */
    public void verHistorico(String numeroConta) {
        findContaByNumero(numeroConta).ifPresentOrElse(Conta::imprimirExtrato, () -> System.out.println("Conta não encontrada."));
    }

    /**
     * Método auxiliar privado para encontrar uma conta pelo número em todo o sistema.
     * @param numero O número da conta a ser encontrada.
     * @return Um Optional contendo a Conta, se existir.
     */
    private Optional<Conta> findContaByNumero(String numero) {
        return clienteRepository.buscarTodos().stream()
                .flatMap(cliente -> cliente.getContas().stream())
                .filter(conta -> conta.getNumero().equals(numero))
                .findFirst();
    }
}