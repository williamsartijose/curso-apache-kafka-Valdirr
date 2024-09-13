package com.wsj.str_consumer.listeners;

import com.wsj.str_consumer.custom.StrConsumerCustomListener;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class StrConsumerListener {

    // Método que consome mensagens do Kafka usando a anotação customizada
    // O grupo de consumidores usado aqui é "group-1"
    @SneakyThrows
    @StrConsumerCustomListener(groupId = "group-1")
    public void create(String message) {
        // Loga a mensagem recebida no tópico Kafka
        log.info("CREATE ::: Receive message {}", message);

        // Força uma exceção para simular um erro no processamento da mensagem
        throw new IllegalArgumentException("EXCEPTION...");
    }

    // Outro método que consome mensagens do Kafka, também com o "group-1"
    // Usando a anotação customizada, ele consome mensagens do tópico padrão definido
    @StrConsumerCustomListener(groupId = "group-1")
    public void log(String message) {
        // Loga a mensagem recebida
        log.info("LOG ::: Receive message {}", message);
    }

    // Método que consome mensagens usando a anotação padrão KafkaListener
    // O grupo de consumidores é "group-2" e o containerFactory é o validMessageContainerFactory
    @KafkaListener(groupId = "group-2", topics = "str-topic", containerFactory = "validMessageContainerFactory")
    public void history(String message) {
        // Loga a mensagem recebida
        log.info("HISTORY ::: Receive message {}", message);
    }
}
