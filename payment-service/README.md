# README - Configuração do Produtor Kafka e Docker

## Introdução

Este projeto configura um produtor Kafka em uma aplicação Spring Boot que envia mensagens no formato JSON. O objetivo é demonstrar como configurar o produtor Kafka, criar um template para envio de mensagens e rodar a aplicação usando Docker e Kafka.

## Estrutura do Projeto

O projeto possui a seguinte estrutura:

```
src/
└── main/
    └── java/
        └── com/
            └── wsj/
                └── paymentservice/
                    └── config/
                        └── JsonProducerConfig.java
docker/
└── kafka/
    ├── Dockerfile
    └── docker-compose.yml
README.md
```

## Configuração do Produtor Kafka

O arquivo `JsonProducerConfig.java` configura o produtor Kafka para enviar mensagens JSON. Abaixo estão os detalhes da configuração:

### `JsonProducerConfig.java`

```java
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
```

## Configuração do Ambiente Docker

Para rodar a aplicação e o Kafka em contêineres Docker, siga as instruções abaixo.

### 1. Dockerfile para Kafka

Crie um arquivo `Dockerfile` no diretório `docker/kafka/` com o seguinte conteúdo:

```Dockerfile
# Usar uma imagem base do Kafka
FROM wurstmeister/kafka:latest

# Adicionar configuração customizada, se necessário
# COPY config/custom-config.properties /opt/kafka/config/

# Expor as portas do Kafka
EXPOSE 9093
```

### 2. docker-compose.yml

Crie um arquivo `docker-compose.yml` no diretório `docker/kafka/` com o seguinte conteúdo:

```yaml
version: '2'

services:
  zookeeper:
    image: wurstmeister/zookeeper:latest
    ports:
      - "2181:2181"

  kafka:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "9093:9093"
    expose:
      - "9093"
    environment:
      KAFKA_ADVERTISED_LISTENERS: INSIDE://kafka:9093,OUTSIDE://localhost:9093
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: INSIDE:PLAINTEXT,OUTSIDE:PLAINTEXT
      KAFKA_LISTENERS: INSIDE://0.0.0.0:9093,OUTSIDE://0.0.0.0:9093
      KAFKA_LISTENER_NAME: INSIDE
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
    depends_on:
      - zookeeper
```

### 3. Rodando o Kafka e Zookeeper

Para subir o Kafka e o Zookeeper, navegue até o diretório `docker/kafka/` e execute:

```bash
docker-compose up
```

### 4. Rodando a Aplicação

Certifique-se de que o Docker e o Kafka estejam rodando. Em seguida, você pode construir e rodar a aplicação Spring Boot da seguinte forma:

```bash
./mvnw clean package
java -jar target/seu-arquivo.jar
```

## Testando a Configuração

1. **Verifique os Logs**: Certifique-se de que não há erros nos logs do Kafka e da aplicação.
2. **Envie Mensagens**: Use a aplicação para enviar mensagens JSON e verifique se elas aparecem no Kafka.

## Conclusão

Você configurou com sucesso um produtor Kafka em uma aplicação Spring Boot e aprendeu a usar Docker para rodar Kafka e Zookeeper. Se houver dúvidas ou problemas, consulte a [documentação do Kafka](https://kafka.apache.org/documentation/) e [Spring Boot Kafka](https://docs.spring.io/spring-kafka/docs/current/reference/html/) para mais detalhes.

