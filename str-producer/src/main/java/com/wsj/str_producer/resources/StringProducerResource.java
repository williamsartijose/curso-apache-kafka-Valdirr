package com.wsj.str_producer.resources;

import com.wsj.str_producer.services.StringProducerService;  // Importa o serviço responsável pelo envio das mensagens para o Kafka.
import lombok.RequiredArgsConstructor;  // Importa a anotação que gera automaticamente um construtor para os campos finais.
import org.springframework.http.HttpStatus;  // Importa as constantes de status HTTP para controle de respostas.
import org.springframework.http.ResponseEntity;  // Usado para criar a resposta HTTP com um status adequado.
import org.springframework.web.bind.annotation.PostMapping;  // Define que o método é um endpoint para requisições HTTP POST.
import org.springframework.web.bind.annotation.RequestBody;  // Anotação para que o corpo da requisição seja passado como parâmetro do método.
import org.springframework.web.bind.annotation.RequestMapping;  // Define o caminho base para todos os endpoints da classe.
import org.springframework.web.bind.annotation.RestController;  // Define que essa classe é um controlador REST, que responde a requisições HTTP.

@RequiredArgsConstructor  // Gera automaticamente um construtor que injeta o `producerService` ao instanciar a classe.
@RestController  // Indica que esta classe é um controlador REST, que manipula requisições HTTP.
@RequestMapping(value = "/producer")  // Define a URL base "/producer" para todos os endpoints da classe.
public class StringProducerResource {

    // Injeção do serviço StringProducerService, que será usado para enviar as mensagens para o Kafka.
    private final StringProducerService producerService;

    // Endpoint que recebe mensagens via HTTP POST.
    @PostMapping  // Indica que esse método será chamado para requisições POST no caminho "/producer".
    public ResponseEntity<?> sendMessage(@RequestBody String message) {
        // Chama o serviço para enviar a mensagem recebida no corpo da requisição para o Kafka.
        producerService.sendMessage(message);
        // Retorna uma resposta HTTP com o status 201 (CREATED) para indicar que a mensagem foi recebida e processada com sucesso.
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
