package com.wsj.paymentservice.config;

import lombok.RequiredArgsConstructor; // Anotação que gera um construtor com todos os campos finais
import org.apache.kafka.clients.admin.AdminClientConfig; // Configurações do cliente Kafka Admin
import org.springframework.boot.autoconfigure.kafka.KafkaProperties; // Propriedades do Kafka configuradas pelo Spring Boot
import org.springframework.context.annotation.Bean; // Define métodos como beans que devem ser gerenciados pelo Spring
import org.springframework.context.annotation.Configuration; // Define a classe como uma configuração Spring
import org.springframework.kafka.config.TopicBuilder; // Utilitário para construir tópicos Kafka
import org.springframework.kafka.core.KafkaAdmin; // Classe para administrar configurações e tópicos do Kafka

import java.util.HashMap; // Importa a classe HashMap para armazenar configurações

@RequiredArgsConstructor
@Configuration // Indica que esta classe contém configurações Spring
public class KafkaAdminConfig {

    // Propriedades do Kafka injetadas pelo Spring
    private final KafkaProperties properties;

    // Bean que cria e configura uma instância de KafkaAdmin
    @Bean
    public KafkaAdmin kafkaAdmin() {
        var configs = new HashMap<String, Object>(); // Mapa para armazenar as configurações do Kafka
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers()); // Configura o servidor Kafka
        return new KafkaAdmin(configs); // Retorna uma instância de KafkaAdmin com as configurações fornecidas
    }

    // Bean que define os tópicos Kafka a serem criados
    @Bean
    public KafkaAdmin.NewTopics newTopics() {
        return new KafkaAdmin.NewTopics(
                // Cria um novo tópico chamado "payment-topic" com 1 partição
                TopicBuilder.name("payment-topic").partitions(1).build()
        );
    }
}
