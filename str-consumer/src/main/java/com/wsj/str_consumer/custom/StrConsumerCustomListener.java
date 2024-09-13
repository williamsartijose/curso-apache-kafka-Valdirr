package com.wsj.str_consumer.custom;

import org.springframework.core.annotation.AliasFor;
import org.springframework.kafka.annotation.KafkaListener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Define a retenção da anotação em tempo de execução (Runtime)
// Isso garante que a anotação estará disponível para ser lida em tempo de execução pela JVM
@Retention(RetentionPolicy.RUNTIME)

// Define o alvo da anotação, que neste caso são métodos
@Target(ElementType.METHOD)

// Anotação customizada que usa a anotação KafkaListener como base
@KafkaListener
public @interface StrConsumerCustomListener {

    // Mapeia o atributo "topics" da anotação KafkaListener para nossa anotação customizada
    // O valor padrão é "str-topic", ou seja, o tópico que será consumido por padrão
    @AliasFor(annotation = KafkaListener.class, attribute = "topics")
    String[] topics() default "str-topic";

    // Mapeia o atributo "containerFactory" da anotação KafkaListener para nossa anotação customizada
    // Define o containerFactory que será usado para consumir as mensagens. O valor padrão é "strContainerFactory"
    @AliasFor(annotation = KafkaListener.class, attribute = "containerFactory")
    String containerFactory() default "strContainerFactory";

    // Mapeia o atributo "groupId" da anotação KafkaListener para nossa anotação customizada
    // Define o grupo de consumidores (groupId) que irá consumir as mensagens
    @AliasFor(annotation = KafkaListener.class, attribute = "groupId")
    String groupId() default "";

    // Mapeia o atributo "errorHandler" da anotação KafkaListener para nossa anotação customizada
    // Define o manipulador de erros a ser usado em caso de falha no consumo das mensagens
    @AliasFor(annotation = KafkaListener.class, attribute = "errorHandler")
    String errorHandler() default "errorCustomHandler";
}
