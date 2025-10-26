package org.example.model;

import lombok.Getter;
import org.example.enums.TipoTransacao;
import org.example.records.Transacao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Abstrata Conta.
 * Representa a essência de uma conta bancária e serve como base para outros tipos de conta (Conta Corrente, Poupança).
 * Não pode ser instanciada diretamente (Abstração).
 * Utiliza Lombok para gerar os getters para todos os campos.
 */
@Getter
public abstract class Conta {

    // Atributos protegidos para serem acessíveis pelas classes filhas (Encapsulamento).
    protected String agencia;
    protected String numero;
    protected double saldo;
    protected Cliente cliente; // Composição: Uma conta "tem-um" cliente.
    protected List<Transacao> historicoTransacoes;

    // Constantes para valores padrão.
    private static final String AGENCIA_PADRAO = "0001";
    private static int SEQUENCIAL = 1; // Variável estática para gerar números de conta únicos.

    /**
     * Construtor da Conta.
     * Inicializa os dados da conta com valores padrão e o cliente fornecido.
     * @param cliente O cliente titular da conta.
     */
    public Conta(Cliente cliente) {
        this.agencia = AGENCIA_PADRAO;
        this.numero = String.format("%04d", SEQUENCIAL++); // Formata o número da conta com 4 dígitos.
        this.cliente = cliente;
        this.saldo = 0.0;
        this.historicoTransacoes = new ArrayList<>(); // Inicializa a lista de transações.
    }

    /**
     * Realiza um depósito na conta.
     * @param valor O valor a ser depositado. Deve ser maior que zero.
     */
    public void depositar(double valor) {
        if (valor > 0) {
            saldo += valor;
            adicionarTransacao(TipoTransacao.DEPOSITO, valor, "Depósito recebido");
        }
    }

    /**
     * Realiza um saque na conta.
     * @param valor O valor a ser sacado. Deve ser maior que zero e menor ou igual ao saldo.
     * @return true se o saque for bem-sucedido, false caso contrário.
     */
    public boolean sacar(double valor) {
        if (valor > 0 && saldo >= valor) {
            saldo -= valor;
            adicionarTransacao(TipoTransacao.SAQUE, valor, "Saque realizado");
            return true;
        }
        System.out.println("Saldo insuficiente ou valor de saque inválido.");
        return false;
    }

    /**
     * Realiza uma transferência para outra conta.
     * Reutiliza os métodos sacar e depositar.
     * @param valor O valor a ser transferido.
     * @param contaDestino A conta que receberá o valor.
     */
    public void transferir(double valor, Conta contaDestino) {
        // Tenta sacar o valor da conta de origem.
        if (this.sacar(valor)) {
            // Se o saque for bem-sucedido, deposita na conta de destino.
            contaDestino.depositar(valor);
            // Registra as transações em ambas as contas para clareza no extrato.
            this.adicionarTransacao(TipoTransacao.TRANSFERENCIA_PIX_ENVIADA, valor, "PIX para " + contaDestino.getCliente().getNome());
            contaDestino.adicionarTransacao(TipoTransacao.TRANSFERENCIA_PIX_RECEBIDA, valor, "PIX de " + this.getCliente().getNome());
        } else {
            System.out.println("Transferência não realizada.");
        }
    }

    /**
     * Adiciona uma nova transação ao histórico da conta.
     * Este é um método auxiliar para manter o código organizado.
     * @param tipo O tipo da transação (usando o Enum).
     * @param valor O valor da transação.
     * @param descricao Uma breve descrição da operação.
     */
    public void adicionarTransacao(TipoTransacao tipo, double valor, String descricao) {
        Transacao transacao = new Transacao(tipo, valor, LocalDateTime.now(), descricao);
        this.historicoTransacoes.add(transacao);
    }

    /**
     * Imprime o extrato completo da conta, incluindo dados do titular e histórico de transações.
     * Este método será sobrescrito nas classes filhas (Polimorfismo).
     */
    public void imprimirExtrato() {
        System.out.println("=== Extrato da Conta ===");
        System.out.printf("Titular: %s%n", this.cliente.getNome());
        System.out.printf("Agência: %s%n", this.agencia);
        System.out.printf("Número: %s%n", this.numero);
        System.out.printf("Saldo: R$ %.2f%n", this.saldo);
        System.out.println("-------------------------");
        System.out.println("Histórico de Transações:");
        if (historicoTransacoes.isEmpty()) {
            System.out.println("Nenhuma transação registrada.");
        } else {
            // Itera sobre o histórico e imprime cada transação.
            historicoTransacoes.forEach(System.out::println);
        }
        System.out.println("========================\n");
    }
}