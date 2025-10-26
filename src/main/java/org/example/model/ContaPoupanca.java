package org.example.model;

/**
 * Representa uma Conta Poupança.
 * Herda todos os atributos e métodos da classe abstrata Conta.
 */
public class ContaPoupanca extends Conta {

    /**
     * Construtor que repassa o cliente para a classe mãe (super).
     * @param cliente O titular da conta.
     */
    public ContaPoupanca(Cliente cliente) {
        super(cliente);
    }

    /**
     * Sobrescreve o método da classe mãe para adicionar um cabeçalho específico.
     * Demonstra o conceito de Polimorfismo.
     */
    @Override
    public void imprimirExtrato() {
        System.out.println("### Extrato Conta Poupança ###");
        super.imprimirExtrato(); // Reutiliza a lógica de impressão da classe pai.
    }
}