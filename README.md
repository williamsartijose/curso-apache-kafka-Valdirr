
# `str-producer` e `str-consumer`

Este projeto é composto por duas aplicações principais: uma para produção de mensagens (`str-producer`) e outra para consumo de mensagens (`str-consumer`) usando Kafka. A configuração é gerenciada com Docker, e o Kafdrop é utilizado para monitorar o Kafka.

## Estrutura do Projeto

### `str-producer`

- **String Producer API**: Um serviço REST que envia mensagens ao Kafka.
- **Kafka**: O broker que recebe e distribui as mensagens.
- **Kafdrop**: Uma interface gráfica que permite visualizar tópicos, mensagens e consumidores.

### `str-consumer`

- **String Consumer**: Um consumidor Kafka que processa mensagens do tópico `str-topic` e possui configuração para diferentes grupos de consumidores e tratamento de erros.

## Pré-requisitos

Antes de começar, certifique-se de ter o seguinte instalado:

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)

## Arquitetura

### `docker-compose.yml`

O arquivo `docker-compose.yml` configura os serviços necessários:

- **Zookeeper**: Gerencia o estado do cluster Kafka.
- **Kafka**: Atua como broker de mensagens.
- **Kafdrop**: Interface gráfica para visualizar e gerenciar o Kafka.
- **str-producer**: Serviço de produção de mensagens.
- **str-consumer**: Serviço de consumo de mensagens.

## Configuração do Docker

### `docker-compose.yml`

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

  str-producer:
    build: ./str-producer
    networks:
      - broker-kafka
    depends_on:
      - kafka
    ports:
      - "8080:8080"

  str-consumer:
    build: ./str-consumer
    networks:
      - broker-kafka
    depends_on:
      - kafka

networks:
  broker-kafka:
    driver: bridge
```

### `Dockerfile` para `str-producer`

```Dockerfile
# Use uma imagem base do OpenJDK
FROM openjdk:17-jdk-slim

# Copie o JAR construído para a imagem Docker
COPY target/str-producer.jar /app/str-producer.jar

# Define o diretório de trabalho
WORKDIR /app

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "str-producer.jar"]
```

### `Dockerfile` para `str-consumer`

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

## Passo a Passo para Rodar os Serviços

### Passo 1: Clone o Repositório

Clone o repositório contendo os projetos `str-producer` e `str-consumer`.

```bash
git clone https://github.com/williamsartijose/str-producer
cd str-producer
```

### Passo 2: Rodar o Docker Compose

Execute o Docker Compose para construir e iniciar os containers:

```bash
docker-compose up --build
```

Isso irá iniciar os serviços `zookeeper`, `kafka`, `kafdrop`, `str-producer` e `str-consumer`.

### Passo 3: Testar a Aplicação `str-producer`

1. **Enviar Mensagens para o Kafka**

   Utilize o endpoint REST para enviar mensagens ao Kafka:

   - **URL do endpoint**:

     ```
     POST http://localhost:8080/producer
     ```

   - **Corpo da requisição**:

     ```json
     {
       "message": "Sua mensagem aqui"
     }
     ```

   - **Exemplo de comando `curl`**:

     ```bash
     curl -X POST http://localhost:8080/producer -H "Content-Type: application/json" -d '{"message": "Olá Kafka!"}'
     ```

2. **Monitorar as Mensagens com Kafdrop**

   Acesse o Kafdrop para visualizar as mensagens enviadas:

   - **URL do Kafdrop**: [http://localhost:19000](http://localhost:19000)
   - Clique no tópico `str-topic` para ver as mensagens.

### Passo 4: Testar a Aplicação `str-consumer`

O `str-consumer` é configurado para consumir mensagens do tópico `str-topic`. As mensagens processadas e os logs podem ser visualizados na saída do Docker:

```bash
docker-compose logs str-consumer
```

### Passo 5: Desligar os Containers

Para parar e remover os containers, execute:

```bash
docker-compose down
```

## Configuração dos Serviços

### `StringProducerFactoryConfig.java`

Configura o produtor Kafka, incluindo a definição dos serializadores e conexão com o Kafka.

### `KafkaAdminConfig.java`

Configura o KafkaAdmin para criar automaticamente o tópico `str-topic` com 2 partições e fator de replicação 1.

### `StringProducerService.java`

Responsável por enviar mensagens ao Kafka e gerar logs de sucesso ou erro.

### `StringProducerResource.java`

Ponto de entrada da API REST para enviar mensagens ao Kafka.

### `StringConsumerConfig.java`

Configura o consumidor Kafka, incluindo deserializadores e configuração de interceptores de registros.

### `StrConsumerCustomListener.java`

Anotação personalizada para simplificar a configuração de consumidores Kafka.

### `StrConsumerListener.java`

Define métodos para consumir mensagens do tópico Kafka e processá-las.

## Comandos Úteis

- **Verificar containers rodando**:

  ```bash
  docker ps
  ```

- **Ver logs dos containers**:

  ```bash
  docker-compose logs
  ```

- **Acessar logs de um serviço específico** (exemplo: Kafka):

  ```bash
  docker-compose logs kafka
  ```

## Referências

- [Docker Compose](https://docs.docker.com/compose/)
- [Kafdrop - Kafka Web UI](https://github.com/obsidiandynamics/kafdrop)
- [Spring Kafka Documentation](https://docs.spring.io/spring-kafka/docs/current/reference/html/)

