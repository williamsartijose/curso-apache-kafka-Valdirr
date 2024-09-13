package com.wsj.str_consumer.exceptions;

import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Log4j2  // Adiciona suporte ao logging com Log4j2
@Component  // Marca a classe como um bean gerenciado pelo Spring
public class ErrorCustomHandler implements KafkaListenerErrorHandler {

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException e) {
        // Loga uma mensagem indicando que um erro foi capturado
        log.info("EXCEPTION_HANDLER ::: Capturei um erro");

        // Loga o payload da mensagem que causou o erro
        log.info("Payload ::: {}", message.getPayload());

        // Loga os headers da mensagem que causou o erro
        log.info("Headers ::: {}", message.getHeaders());

        // Loga o offset da mensagem, que é um identificador único dentro do tópico Kafka
        log.info("Offset ::: {}", message.getHeaders().get("kafka_offset"));

        // Loga a mensagem da exceção capturada
        log.info("Message exception ::: {}", e.getMessage());

        // Retorna null, pois o tratamento de erro não deve alterar o fluxo de processamento da mensagem
        return null;
    }

    // O método abaixo está comentado, mas é uma alternativa para o tratamento de erros
    // @Override
    // public Object handleError(Message<?> message, ListenerExecutionFailedException exception, Consumer<?, ?> consumer) {
    //     return KafkaListenerErrorHandler.super.handleError(message, exception, consumer);
    // }
}
