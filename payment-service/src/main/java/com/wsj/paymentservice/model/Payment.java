package com.wsj.paymentservice.model;

import lombok.Getter; // Gera automaticamente os métodos getter para os campos
import java.io.Serializable; // Interface que marca os objetos que podem ser serializados

// Classe que representa um pagamento
@Getter // Gera automaticamente os métodos getter para todos os campos
public class Payment implements Serializable {

    private Long id; // Identificador único do pagamento
    private Long idUser; // Identificador do usuário que fez o pagamento
    private Long idProduct; // Identificador do produto associado ao pagamento
    private String cardNumber; // Número do cartão de crédito usado para o pagamento
}
