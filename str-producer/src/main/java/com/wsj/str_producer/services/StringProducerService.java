package com.wsj.str_producer.services;

import lombok.RequiredArgsConstructor;  // Importa a anotação para gerar o construtor automaticamente para os campos finais.
import lombok.extern.log4j.Log4j2;  // Importa a anotação para o uso do Log4j2 para geração de logs.
import org.springframework.kafka.core.KafkaTemplate;  // Importa a classe que permite interagir com o Kafka para enviar mensagens.
import org.springframework.stereotype.Service;  // Indica que esta classe é um serviço do Spring.

@Log4j2  // Habilita o uso do Log4j2 para geração de logs.
@RequiredArgsConstructor  // Gera automaticamente um construtor para os campos marcados como 'final'.
@Service  // Indica que esta classe é um serviço gerenciado pelo Spring.
public class StringProducerService {

    // Campo que será inicializado via injeção de dependência com o KafkaTemplate para enviar mensagens.
    private final KafkaTemplate<String, String> kafkaTemplate;

    // Método que envia uma mensagem para o tópico 'str-topic' no Kafka.
    public void sendMessage(String message) {
        // Envia a mensagem para o tópico 'str-topic'.
        kafkaTemplate.send("str-topic", message).addCallback(
                // Callback executado no sucesso do envio da mensagem.
                success -> {
                    if(success != null) {
                        // Loga a mensagem enviada com sucesso.
                        log.info("Send message with success {}", message);
                        // Loga a partição e o offset onde a mensagem foi gravada no Kafka.
                        log.info("Partition {}, Offset {}",
                                success.getRecordMetadata().partition(),
                                success.getRecordMetadata().offset());
                    }
                },
                // Callback executado em caso de erro no envio da mensagem.
                error -> log.error("Error send message")  // Loga um erro caso a mensagem não seja enviada.
        );
    }
}
