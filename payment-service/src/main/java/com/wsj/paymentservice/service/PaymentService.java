package com.wsj.paymentservice.service;

import com.wsj.paymentservice.model.Payment; // Importa o modelo de pagamento

// Interface responsável por definir o contrato do serviço de pagamento
public interface PaymentService {

    // Método que deve ser implementado para enviar o pagamento
    // Recebe um objeto do tipo Payment como parâmetro
    void sendPayment(Payment payment);
}
