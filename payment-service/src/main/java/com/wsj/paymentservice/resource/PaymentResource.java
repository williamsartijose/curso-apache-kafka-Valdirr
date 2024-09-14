package com.wsj.paymentservice.resource;

import com.wsj.paymentservice.model.Payment; // Modelo de pagamento
import org.springframework.http.ResponseEntity; // Representa a resposta HTTP
import org.springframework.web.bind.annotation.PostMapping; // Define que o método será chamado via requisição POST
import org.springframework.web.bind.annotation.RequestBody; // Indica que o parâmetro será enviado no corpo da requisição

// Interface responsável por definir o contrato do recurso de pagamento
public interface PaymentResource {

    // Método que processa uma requisição POST para realizar o pagamento
    // Recebe o objeto Payment enviado no corpo da requisição HTTP
    @PostMapping
    ResponseEntity<Payment> payment(@RequestBody Payment payment);
}
