package org.example.enums;

/**
 * Enum para representar os tipos de transações financeiras possíveis.
 * O uso de um enum torna o código mais seguro e legível, evitando o uso de strings ou números mágicos.
 */
public enum TipoTransacao {
    DEPOSITO,
    SAQUE,
    TRANSFERENCIA_PIX_ENVIADA,
    TRANSFERENCIA_PIX_RECEBIDA,
    CRIACAO_INVESTIMENTO
}