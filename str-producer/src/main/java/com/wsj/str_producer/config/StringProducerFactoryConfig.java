package com.wsj.str_producer.config;

import lombok.RequiredArgsConstructor;  // Importa a anotação para gerar automaticamente um construtor com os campos finais.
import org.apache.kafka.clients.producer.ProducerConfig;  // Importa configurações específicas do produtor Kafka.
import org.apache.kafka.common.serialization.StringSerializer;  // Importa o serializador de strings para chave e valor.
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;  // Importa propriedades do Kafka definidas no arquivo de configuração.
import org.springframework.context.annotation.Bean;  // Define que o método será gerenciado pelo Spring e produzirá um bean.
import org.springframework.context.annotation.Configuration;  // Indica que essa classe contém a configuração do Kafka.
import org.springframework.kafka.core.DefaultKafkaProducerFactory;  // Cria uma fábrica de produtores Kafka padrão.
import org.springframework.kafka.core.KafkaTemplate;  // Facilita o envio de mensagens para o Kafka.
import org.springframework.kafka.core.ProducerFactory;  // Interface para definir uma fábrica de produtores.

import java.util.HashMap;  // Importa a classe que permite armazenar as configurações do produtor em um mapa chave-valor.

@RequiredArgsConstructor  // Gera automaticamente o construtor para inicializar o campo 'properties'.
@Configuration  // Indica que essa classe será usada para configuração no contexto do Spring.
public class StringProducerFactoryConfig {

    // Propriedades do Kafka que são automaticamente injetadas, contendo as configurações definidas no arquivo de propriedades.
    private final KafkaProperties properties;

    // Define um bean que cria a fábrica de produtores Kafka.
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        // Cria um HashMap para armazenar as configurações do produtor Kafka.
        var configs = new HashMap<String, Object>();
        // Define o endereço do servidor Kafka (Bootstrap Servers) a partir das propriedades configuradas.
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        // Define o serializador da chave (String) para garantir que as chaves enviadas sejam convertidas para bytes.
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // Define o serializador do valor (String) para garantir que os valores enviados sejam convertidos para bytes.
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // Retorna uma fábrica de produtores Kafka com as configurações especificadas.
        return new DefaultKafkaProducerFactory<>(configs);
    }

    // Define um bean que cria o KafkaTemplate, facilitando o envio de mensagens para o Kafka.
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        // Retorna o KafkaTemplate usando a fábrica de produtores configurada acima.
        return new KafkaTemplate<>(producerFactory);
    }
}
