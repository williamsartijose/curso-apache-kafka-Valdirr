

# String Producer Kafka Application

Este projeto é uma aplicação simples de produção de mensagens usando Kafka, Zookeeper e Kafdrop para monitoramento. A aplicação produz mensagens em um tópico Kafka através de um serviço REST. Utiliza o Docker para configurar o ambiente necessário com Zookeeper, Kafka e Kafdrop.

## Pré-requisitos

Antes de começar, certifique-se de ter o seguinte instalado:

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)

## Estrutura do Projeto

- **Zookeeper**: Gerencia o estado do cluster Kafka.
- **Kafka**: Atua como broker de mensagens, onde as mensagens são publicadas.
- **Kafdrop**: Interface gráfica para visualizar e gerenciar o Kafka.

## Arquitetura

A aplicação consiste em três serviços principais:

- **String Producer API**: Um serviço REST que envia mensagens ao Kafka.
- **Kafka**: O broker que recebe e distribui as mensagens.
- **Kafdrop**: Uma interface gráfica que permite visualizar tópicos, mensagens e consumidores.

### Dependências

- Spring Boot
- Kafka
- Docker
- Kafdrop (visualização de tópicos Kafka)

## Instruções de Uso

### Passo 1: Configuração com Docker

Aqui está o arquivo `docker-compose.yml` que configura os serviços necessários para rodar o Kafka e o Kafdrop:

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

networks:
  broker-kafka:
    driver: bridge
```

### Passo 2: Rodar os containers Docker

1. **Clone o repositório**:

```bash
git clone https://github.com/williamsartijose/str-producer
cd str-producer
```

2. **Rodar o Docker Compose**:

```bash
docker-compose up -d
```

- Isso vai inicializar os serviços Zookeeper, Kafka e Kafdrop.
- O Kafka estará acessível na porta `9092`.
- O Kafdrop estará acessível via navegador na URL: [http://localhost:19000](http://localhost:19000).

### Passo 3: Testando a Aplicação String Producer

A aplicação expõe um endpoint REST para enviar mensagens ao Kafka.

1. **URL do endpoint**:

```
POST http://localhost:8080/producer
```

2. **Corpo da requisição**:

```json
{
  "message": "Sua mensagem aqui"
}
```

Você pode enviar essa requisição usando uma ferramenta como `Postman`, `cURL` ou diretamente de seu frontend. Exemplo de comando `curl`:

```bash
curl -X POST http://localhost:8080/producer -H "Content-Type: application/json" -d '{"message": "Olá Kafka!"}'
```

### Passo 4: Monitorando as Mensagens com Kafdrop

Para visualizar as mensagens que foram enviadas para o Kafka:

1. Acesse o **Kafdrop** via navegador: [http://localhost:19000](http://localhost:19000)
2. Você verá a lista de tópicos, incluindo o tópico `str-topic` que foi criado para o projeto.
3. Clique no tópico `str-topic` para visualizar as mensagens enviadas.

### Passo 5: Desligando os Containers

Após finalizar o uso dos serviços, você pode parar os containers rodando:

```bash
docker-compose down
```

Isso vai parar e remover os containers Docker associados ao Kafka, Zookeeper e Kafdrop.

## Configuração do Projeto

### Kafka Producer Config (`StringProducerFactoryConfig.java`)

Esta classe configura o produtor Kafka que será usado pela aplicação para enviar mensagens para o tópico Kafka.

- Conecta ao Zookeeper para garantir que o Kafka está operando corretamente.
- Define o serializador de chave e valor como `StringSerializer`, uma vez que trabalhamos com strings.

### Kafka Admin Config (`KafkaAdminConfig.java`)

Essa classe configura o KafkaAdmin, que permite a criação automática de tópicos quando a aplicação é iniciada.

- O tópico `str-topic` é criado com 2 partições e fator de replicação 1.

### String Producer Service (`StringProducerService.java`)

Este serviço é responsável por enviar mensagens para o Kafka.

- Usa o `KafkaTemplate` para enviar mensagens ao tópico `str-topic`.
- Logs são gerados indicando se a mensagem foi enviada com sucesso ou se ocorreu algum erro.

### String Producer Resource (`StringProducerResource.java`)

Este é o ponto de entrada da API REST.

- Recebe requisições POST com mensagens no corpo e as envia para o Kafka.
- Retorna uma resposta HTTP com o status `201 CREATED` ao enviar a mensagem.

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

