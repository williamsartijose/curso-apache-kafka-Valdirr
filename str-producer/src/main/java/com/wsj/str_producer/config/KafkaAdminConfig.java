package com.wsj.str_producer.config;

import lombok.RequiredArgsConstructor;  // Importa a anotação para gerar automaticamente um construtor com os campos finais.
import org.apache.kafka.clients.admin.AdminClientConfig;  // Importa configurações específicas do cliente administrador do Kafka.
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;  // Importa as propriedades do Kafka definidas no arquivo de configuração.
import org.springframework.context.annotation.Bean;  // Define que o método produzirá um bean gerenciado pelo Spring.
import org.springframework.context.annotation.Configuration;  // Indica que esta classe é uma configuração do Spring.
import org.springframework.kafka.config.TopicBuilder;  // Facilita a criação e configuração de tópicos no Kafka.
import org.springframework.kafka.core.KafkaAdmin;  // Responsável pela administração de tópicos e outras operações administrativas no Kafka.

import java.util.HashMap;  // Importa a classe que permite armazenar configurações em um mapa chave-valor.

@RequiredArgsConstructor  // Gera automaticamente o construtor para inicializar o campo 'properties'.
@Configuration  // Indica que essa classe será usada para configuração no contexto do Spring.
public class KafkaAdminConfig {

    // Propriedades do Kafka que são automaticamente injetadas, contendo as configurações definidas no arquivo de propriedades.
    public final KafkaProperties properties;

    // Define um bean que cria um KafkaAdmin, responsável por operações administrativas no Kafka, como criar tópicos.
    @Bean
    public KafkaAdmin kafkaAdmin() {
        // Cria um HashMap para armazenar as configurações do KafkaAdmin.
        var configs = new HashMap<String, Object>();
        // Define o endereço dos servidores Kafka (Bootstrap Servers) a partir das propriedades configuradas.
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        // Retorna uma instância de KafkaAdmin configurada para se conectar aos servidores especificados.
        return new KafkaAdmin(configs);
    }

    // Define um bean que cria os tópicos no Kafka utilizando o KafkaAdmin.
    @Bean
    public KafkaAdmin.NewTopics topics() {
        // Cria um novo tópico chamado 'str-topic' com 2 partições e 1 réplica.
        return new KafkaAdmin.NewTopics(
                TopicBuilder.name("str-topic")  // Nome do tópico.
                        .partitions(2)  // Define o número de partições para o tópico.
                        .replicas(1)  // Define o número de réplicas do tópico (1 neste caso).
                        .build()  // Constrói a configuração do tópico.
        );
    }
}
