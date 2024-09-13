package com.wsj.str_consumer.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.RecordInterceptor;

import java.util.HashMap;

@Log4j2
@RequiredArgsConstructor
@Configuration
public class StringConsumerConfig {

    // Injeta automaticamente as propriedades de configuração do Kafka definidas no arquivo application.yml
    private final KafkaProperties properties;

    // Configuração do factory do consumidor Kafka
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        // Cria um mapa de configurações para o consumidor Kafka
        var configs = new HashMap<String, Object>();

        // Define o endereço do Kafka Broker
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());

        // Configura o deserializador de chave (key) como String
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // Configura o deserializador de valor (value) como String
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // Retorna uma fábrica de consumidores com as configurações especificadas
        return new DefaultKafkaConsumerFactory<>(configs);
    }

    // Cria a configuração para o container que irá escutar mensagens do Kafka
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> strContainerFactory(
            ConsumerFactory<String, String> consumerFactory
    ) {
        // Cria uma nova fábrica de listeners para consumir as mensagens do Kafka
        var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();

        // Associa a fábrica de consumidores criada anteriormente
        factory.setConsumerFactory(consumerFactory);

        // Retorna a fábrica de listeners configurada
        return factory;
    }

    // Configura um container com interceptação de mensagens válidas
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> validMessageContainerFactory(
            ConsumerFactory<String, String> consumerFactory
    ) {
        // Cria uma nova fábrica de listeners com interceptação de mensagens
        var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();

        // Associa a fábrica de consumidores padrão
        factory.setConsumerFactory(consumerFactory);

        // Define um interceptor para validar as mensagens recebidas
        factory.setRecordInterceptor(validMessage());

        // Retorna a fábrica de listeners configurada com interceptor
        return factory;
    }

    // Intercepta e valida as mensagens consumidas do Kafka
    private RecordInterceptor<String, String> validMessage() {
        return record -> {
            // Verifica se a mensagem contém a palavra "Teste"
            if(record.value().contains("Teste")) {
                // Loga uma mensagem se "Teste" estiver presente
                log.info("Possui a palavra Teste");

                // Retorna o registro para continuar o processamento
                return record;
            }

            // Se a mensagem não contiver "Teste", simplesmente retorna o registro
            return record;
        };
    }

}
