
# `str-consumer`

Este projeto é um consumidor Kafka desenvolvido com Spring Boot. Ele consome mensagens de um tópico Kafka e possui configuração para diferentes grupos de consumidores e tratamento de erros.

## Configuração do Projeto

### `StrConsumerListener`

A classe `StrConsumerListener` contém métodos que consomem mensagens de Kafka. Ela utiliza diferentes configurações de consumidores para demonstrar como os consumidores Kafka podem ser configurados e utilizados.

### `StrConsumerCustomListener`

A anotação `StrConsumerCustomListener` é uma anotação personalizada para simplificar a configuração de consumidores Kafka. Ela permite definir tópicos, grupos de consumidores, e manipuladores de erro de forma declarativa.

### `StringConsumerConfig`

A configuração de consumidores Kafka está na classe `StringConsumerConfig`, que define a configuração do `ConsumerFactory` e dos `KafkaListenerContainerFactory`. Essa classe configura o consumidor para deserializar mensagens e pode incluir interceptores de registros.

## Configuração do `application.yml`

O arquivo `application.yml` define a configuração do servidor e do consumidor Kafka:

```yaml
server:
  port: 8100  # Porta em que a aplicação será executada

spring:
  kafka:
    consumer:
      bootstrap-servers: localhost:29092  # Endereço do servidor Kafka
```

## Executando a Aplicação com Docker

### Passo a Passo

1. **Certifique-se de ter o Docker e o Docker Compose instalados em seu sistema.**

2. **Crie o arquivo `docker-compose.yml` na raiz do seu projeto com a configuração abaixo:**

    ```yaml
    version: '3'

    services:
      zookeeper:
        image: confluentinc/cp-zookeeper:latest
        networks:
          - broker-kafka
        environment:
          ZOOKEEPER_CLIENT_PORT: 2181
          ZOOKEEPER_TICK_TIME: 2000

      kafka:
        image: confluentinc/cp-kafka:latest
        networks:
          - broker-kafka
        depends_on:
          - zookeeper
        ports:
          - "9092:9092"
        environment:
          KAFKA_BROKER_ID: 1
          KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
          KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:29092,PLAINTEXT_HOST://localhost:9092
          KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
          KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
          KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1

      str-consumer:
        build: .
        networks:
          - broker-kafka
        depends_on:
          - kafka
        ports:
          - "8100:8100"

      kafdrop:
        image: obsidiandynamics/kafdrop:latest
        networks:
          - broker-kafka
        depends_on:
          - kafka
        ports:
          - "19000:9000"
        environment:
          KAFKA_BROKERCONNECT: kafka:29092

    networks:
      broker-kafka:
        driver: bridge
    ```

3. **Crie um `Dockerfile` na raiz do projeto para a aplicação `str-consumer`:**

    ```Dockerfile
    # Use uma imagem base do OpenJDK
    FROM openjdk:17-jdk-slim

    # Copie o JAR construído para a imagem Docker
    COPY target/str-consumer.jar /app/str-consumer.jar

    # Define o diretório de trabalho
    WORKDIR /app

    # Comando para executar a aplicação
    ENTRYPOINT ["java", "-jar", "str-consumer.jar"]
    ```

4. **Construa e inicie os containers Docker:**

   Navegue até o diretório onde está o `docker-compose.yml` e execute o seguinte comando:

    ```bash
    docker-compose up --build
    ```

   Esse comando construirá a imagem Docker para a aplicação `str-consumer` e iniciará todos os serviços definidos no `docker-compose.yml`, incluindo Zookeeper, Kafka, a aplicação `str-consumer` e o Kafdrop.

5. **Verifique se tudo está funcionando corretamente:**

    - A aplicação `str-consumer` estará disponível em `http://localhost:8100`.
    - O Kafdrop, uma interface web para visualizar tópicos e mensagens Kafka, estará disponível em `http://localhost:19000`.

## Testando a Aplicação

1. **Enviando Mensagens para o Tópico:**

   Para testar o consumo de mensagens, você pode usar o Kafdrop para enviar mensagens para o tópico `str-topic`.

2. **Verificando Logs:**

   Verifique os logs da aplicação `str-consumer` para ver como as mensagens estão sendo consumidas. Logs são visíveis na saída do Docker ou através do comando:

   ```bash
   docker logs <container_id>
   ```

   Substitua `<container_id>` pelo ID do container da aplicação `str-consumer`, que pode ser obtido com `docker ps`.

