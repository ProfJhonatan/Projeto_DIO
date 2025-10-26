package org.example.records;

import org.example.enums.TipoTransacao;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Record para representar uma transação de forma imutável.
 * Records são ideais para classes que são simples contêineres de dados.
 * O compilador gera automaticamente construtor, getters, equals(), hashCode() e toString().
 */
public record Transacao(TipoTransacao tipo, double valor, LocalDateTime data, String descricao) {

    // Formatter para exibir a data e hora de forma legível.
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /**
     * Sobrescreve o método toString() para formatar a exibição da transação no extrato.
     * @return Uma string formatada representando a transação.
     */
    @Override
    public String toString() {
        return String.format("[%s] %-28s | Valor: R$ %.2f | %s",
                data.format(formatter), tipo, valor, descricao);
    }
}