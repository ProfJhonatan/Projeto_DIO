package org.example.service;

import org.example.enums.TipoTransacao;
import org.example.model.*;
import org.example.repository.ContaRepository;

/**
 * Camada de Serviço.
 * Contém a lógica de negócio da aplicação, orquestrando as operações entre a UI e o Repositório.
 */
public class ContaService {

    private final ContaRepository repository; // Dependência do repositório (Injeção de Dependência).

    public ContaService(ContaRepository repository) {
        this.repository = repository;
    }

    /**
     * Cria uma nova conta (Corrente ou Poupança) e a salva no repositório.
     * @param cliente O cliente titular.
     * @param tipoConta O tipo da conta ("corrente" ou "poupanca").
     * @return A conta recém-criada.
     */
    public Conta criarConta(Cliente cliente, String tipoConta) {
        Conta novaConta;
        if ("corrente".equalsIgnoreCase(tipoConta)) {
            novaConta = new ContaCorrente(cliente);
        } else {
            novaConta = new ContaPoupanca(cliente);
        }
        repository.salvar(novaConta);
        return novaConta;
    }

    /**
     * Executa a lógica de depósito.
     */
    public void depositar(String numeroConta, double valor) {
        repository.buscarPorNumero(numeroConta)
                .ifPresentOrElse(
                        conta -> conta.depositar(valor),
                        () -> System.out.println("Erro: Conta não encontrada.")
                );
    }

    /**
     * Executa a lógica de saque.
     */
    public void sacar(String numeroConta, double valor) {
        repository.buscarPorNumero(numeroConta)
                .ifPresentOrElse(
                        conta -> conta.sacar(valor),
                        () -> System.out.println("Erro: Conta não encontrada.")
                );
    }

    /**
     * Executa a lógica de transferência entre duas contas.
     */
    public void transferir(String numContaOrigem, String numContaDestino, double valor) {
        var contaOrigemOpt = repository.buscarPorNumero(numContaOrigem);
        var contaDestinoOpt = repository.buscarPorNumero(numContaDestino);

        if (contaOrigemOpt.isPresent() && contaDestinoOpt.isPresent()) {
            contaOrigemOpt.get().transferir(valor, contaDestinoOpt.get());
        } else {
            System.out.println("Erro: Conta de origem ou destino não encontrada.");
        }
    }

    /**
     * Cria um investimento, debitando o valor da conta do cliente.
     */
    public void criarInvestimento(String numeroConta, String nomeInvestimento, double valor) {
        repository.buscarPorNumero(numeroConta).ifPresentOrElse(conta -> {
            if (conta.sacar(valor)) { // Reutiliza o método sacar para debitar o valor
                new Investimento(nomeInvestimento, valor); // Cria o objeto Investimento (não é armazenado neste exemplo)
                conta.adicionarTransacao(TipoTransacao.CRIACAO_INVESTIMENTO, valor, "Aplicação em " + nomeInvestimento);
                System.out.println("Investimento criado com sucesso!");
            }
        }, () -> System.out.println("Erro: Conta não encontrada."));
    }

    /**
     * Solicita a impressão do extrato de uma conta.
     */
    public void consultarExtrato(String numeroConta) {
        repository.buscarPorNumero(numeroConta)
                .ifPresentOrElse(
                        Conta::imprimirExtrato, // Usa Method Reference para um código mais limpo
                        () -> System.out.println("Erro: Conta não encontrada.")
                );
    }
}