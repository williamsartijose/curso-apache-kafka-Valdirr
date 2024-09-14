package com.wsj.paymentservice.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Configuração do produtor Kafka para envio de mensagens em formato JSON.
 * A configuração é responsável por definir como o produtor Kafka deve se comportar,
 * incluindo os detalhes de serialização e as propriedades de conexão com o broker Kafka.
 */
@RequiredArgsConstructor
@Configuration
public class JsonProducerConfig {

    // Propriedades do Kafka injetadas pelo Spring Boot
    private final KafkaProperties properties;

    /**
     * Cria uma fábrica de produtores Kafka com configurações específicas.
     *
     * @return A fábrica de produtores configurada para enviar mensagens em formato JSON.
     */
    @Bean
    public ProducerFactory jsonProducerFactory() {
        var configs = new HashMap<String, Object>();

        // Configura o endereço dos brokers Kafka
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());

        // Configura o serializador para as chaves das mensagens como String
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // Configura o serializador para os valores das mensagens como JSON
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        // Cria e retorna uma fábrica de produtores Kafka com as configurações especificadas
        return new DefaultKafkaProducerFactory(configs, new StringSerializer(), new JsonSerializer());
    }

    /**
     * Cria um template Kafka para enviar mensagens com as configurações do produtor.
     *
     * @param jsonProducerFactory Fábrica de produtores Kafka usada para criar o template.
     * @return Um template Kafka configurado para enviar mensagens com formato JSON.
     */
    @Bean
    public KafkaTemplate<String, Serializable> jsonKafkaTemplate(ProducerFactory jsonProducerFactory) {
        return new KafkaTemplate<>(jsonProducerFactory);
    }
}
