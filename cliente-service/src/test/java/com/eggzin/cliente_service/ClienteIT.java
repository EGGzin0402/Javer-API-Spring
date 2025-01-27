package com.eggzin.cliente_service;

import com.eggzin.cliente_service.web.dto.ClienteCreateDto;
import com.eggzin.cliente_service.web.dto.ClienteEditDto;
import com.eggzin.cliente_service.web.dto.ClienteResponseDto;
import com.eggzin.cliente_service.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClienteIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void createCliente_ComDadosValidos_RetornarClienteCriadoComStatus201() {
        ClienteResponseDto responseBody = testClient
                .post()
                .uri("/cliente-service/clientes")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"pedro@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClienteCreateDto("Pedro Barbosa", "11944643410", true))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ClienteResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getNome()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getTelefone()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getNome()).isEqualTo("Pedro Barbosa");
        org.assertj.core.api.Assertions.assertThat(responseBody.getTelefone()).isEqualTo(11944643410L);
        org.assertj.core.api.Assertions.assertThat(responseBody.isCorrentista()).isEqualTo(true);
    }

    @Test
    public void createCliente_SemAutenticacao_Retornar401() {
        var responseBody = testClient
                .post()
                .uri("/cliente-service/clientes")
                .headers(httpHeaders -> httpHeaders.add(HttpHeaders.AUTHORIZATION, null))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClienteCreateDto("", "", false)) // Dados inválidos
                .exchange();

        responseBody.expectStatus().isUnauthorized();

        org.assertj.core.api.Assertions.assertThat(responseBody.expectBody().returnResult().getResponseBody()).isNull();
    }

    @Test
    public void createCliente_ClienteJaExistente_Retornar409() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/cliente-service/clientes")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClienteCreateDto("Ana Silva", "11944643410", true)) // Dados inválidos
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
    }

    @Test
    public void createCliente_ComDadosInvalidos_Retornar422() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/cliente-service/clientes")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClienteCreateDto("", "", false)) // Dados inválidos
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void findByID_ClienteExistenteERelacionadoAoUsuario_Retornar200() {
        ClienteResponseDto responseBody = testClient
                .get()
                .uri("/cliente-service/clientes/10") // ID de cliente existente
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClienteResponseDto.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getNome()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getTelefone()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getNome()).isEqualTo("Ana Silva");
        org.assertj.core.api.Assertions.assertThat(responseBody.getTelefone()).isEqualTo(11987654321L);
    }

    @Test
    public void findByID_SemAutenticacao_Retornar401() {
        var response = testClient
                .get()
                .uri("/cliente-service/clientes/20") // ID de cliente existente
                .headers(httpHeaders -> httpHeaders.add(HttpHeaders.AUTHORIZATION, null))
                .exchange();

        response.expectStatus().isUnauthorized();

        org.assertj.core.api.Assertions.assertThat(response.expectBody().returnResult().getResponseBody()).isNull();

        response = testClient
                .get()
                .uri("/cliente-service/clientes/20") // ID de cliente existente
                .headers(httpHeaders -> httpHeaders.add(HttpHeaders.AUTHORIZATION, ""))
                .exchange();

        response.expectStatus().isUnauthorized();

        org.assertj.core.api.Assertions.assertThat(response.expectBody().returnResult().getResponseBody()).isNull();

    }

    @Test
    public void findByID_ClienteNaoRelacionadoAoUsuario_Retornar403() {
        var response = testClient
                .get()
                .uri("/cliente-service/clientes/20") // ID de cliente existente
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "pedro@email.com", "123456"))
                .exchange();

        response.expectStatus().isForbidden();

        org.assertj.core.api.Assertions.assertThat(response.expectBody().returnResult().getResponseBody()).isNull();
    }

    @Test
    public void findByID_ClienteNaoExistente_Retornar404() {
        ErrorMessage responseBody = testClient
                .get()
                .uri("/cliente-service/clientes/99999") // ID inexistente
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void update_ClienteExistenteERelacionadoAoUsuario_Retornar200() {
        ClienteEditDto editDto = new ClienteEditDto("Novo Nome", "11999999999");
        ClienteResponseDto responseBody = testClient
                .patch()
                .uri("/cliente-service/clientes/10") // ID de cliente existente
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(editDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClienteResponseDto.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getNome()).isEqualTo("Novo Nome");
        org.assertj.core.api.Assertions.assertThat(responseBody.getTelefone()).isEqualTo(11999999999L);
    }


    @Test
    public void update_ClienteNaoRelacionadoAoUsuario_Retornar403() {
        ClienteEditDto editDto = new ClienteEditDto("Novo Nome", "11999999999");
        var response = testClient
                .patch()
                .uri("/cliente-service/clientes/20") // ID de cliente existente
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "pedro@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(editDto)
                .exchange();

        response.expectStatus().isForbidden();

        org.assertj.core.api.Assertions.assertThat(response.expectBody().returnResult().getResponseBody()).isNull();
    }

    @Test
    public void update_ClienteNaoExistente_Retornar404() {
        ClienteEditDto editDto = new ClienteEditDto("Novo Nome", "11999999999");
        ErrorMessage responseBody = testClient
                .patch()
                .uri("/cliente-service/clientes/99999") // ID inexistente
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(editDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void update_ComDadosInvalidos_Retornar422() {
        ClienteEditDto editDto = new ClienteEditDto("", ""); // Dados inválidos
        ErrorMessage responseBody = testClient
                .patch()
                .uri("/cliente-service/clientes/20")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joao@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(editDto)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void delete_ClienteExistenteERelacionadoAoUsuario_Retornar204() {
        testClient
                .delete()
                .uri("/cliente-service/clientes/10") // ID de cliente existente
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty(); // Não há corpo na resposta para status 204

        ErrorMessage responseBody = testClient
                .get()
                .uri("/cliente-service/clientes/10") // ID inexistente
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);

    }

    @Test
    public void delete_ClienteNaoRelacionadoAoUsuario_Retornar403() {
        var response = testClient
                .delete()
                .uri("/cliente-service/clientes/20") // ID de cliente existente
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "pedro@email.com", "123456"))
                .exchange();

        response.expectStatus().isForbidden();

        org.assertj.core.api.Assertions.assertThat(response.expectBody().returnResult().getResponseBody()).isNull();
    }

    @Test
    public void delete_ClienteNaoExistente_Retornar404() {
        ErrorMessage responseBody = testClient
                .delete()
                .uri("/cliente-service/clientes/99999") // ID inexistente
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "lucia@email.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }
    
}
