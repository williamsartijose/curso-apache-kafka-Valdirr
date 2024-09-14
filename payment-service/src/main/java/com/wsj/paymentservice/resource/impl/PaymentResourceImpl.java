package com.wsj.paymentservice.resource.impl;

import com.wsj.paymentservice.model.Payment; // Modelo de pagamento
import com.wsj.paymentservice.resource.PaymentResource; // Interface do recurso de pagamento
import com.wsj.paymentservice.service.PaymentService; // Serviço de pagamento
import lombok.RequiredArgsConstructor; // Anotação que gera um construtor com todos os campos finais
import org.springframework.http.HttpStatus; // Representa o status HTTP
import org.springframework.http.ResponseEntity; // Representa a resposta HTTP
import org.springframework.web.bind.annotation.RequestMapping; // Define o mapeamento de URL
import org.springframework.web.bind.annotation.RestController; // Define a classe como um controlador REST

// Gera automaticamente um construtor com todos os campos finais
@RequiredArgsConstructor
// Define que essa classe é um controlador REST do Spring
@RestController
// Define o mapeamento base para a URL /payments
@RequestMapping(value = "/payments")
public class PaymentResourceImpl implements PaymentResource {

    // Serviço de pagamento que será injetado pelo Spring
    private final PaymentService paymentService;

    // Implementação do método de pagamento, que recebe um objeto Payment e retorna uma resposta HTTP
    @Override
    public ResponseEntity<Payment> payment(Payment payment) {
        // Chama o serviço de pagamento para processar o pagamento recebido
        paymentService.sendPayment(payment);

        // Retorna uma resposta HTTP com o status 201 (CREATED), indicando que o recurso foi criado
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
