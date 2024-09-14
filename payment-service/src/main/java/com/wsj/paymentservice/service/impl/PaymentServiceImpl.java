package com.wsj.paymentservice.service.impl;

import com.wsj.paymentservice.model.Payment; // Modelo de pagamento
import com.wsj.paymentservice.service.PaymentService; // Interface do serviço de pagamento
import lombok.RequiredArgsConstructor; // Anotação que gera um construtor com todos os campos finais
import lombok.SneakyThrows; // Permite lançar exceções sem explicitamente declará-las
import lombok.extern.log4j.Log4j2; // Para logar mensagens usando Log4j2
import org.springframework.kafka.core.KafkaTemplate; // Classe para enviar mensagens para um tópico Kafka
import org.springframework.stereotype.Service; // Define essa classe como um serviço Spring

import java.io.Serializable; // Interface que marca os objetos que podem ser serializados

// Gera automaticamente um construtor com todos os campos finais
@RequiredArgsConstructor
// Habilita o uso do Log4j2 para registrar logs
@Log4j2
// Define que essa classe é um serviço gerenciado pelo Spring
@Service
public class PaymentServiceImpl implements PaymentService {

    // Template do Kafka para enviar mensagens, nesse caso, serializáveis
    private final KafkaTemplate<String, Serializable> kafkaTemplate;

    // Permite lançar exceções sem explicitamente declará-las no método
    @SneakyThrows
    @Override
    public void sendPayment(Payment payment) {
        // Loga que o pagamento foi recebido, exibindo os detalhes do pagamento
        log.info("Recebi o pagamento {}", payment);

        // Simula um atraso de 1 segundo antes de enviar o pagamento
        Thread.sleep(1000);

        // Loga que o pagamento está sendo enviado
        log.info("Enviando pagamento...");

        // Envia a mensagem do pagamento para o tópico "payment-topic" no Kafka
        kafkaTemplate.send("payment-topic", payment);
    }
}
