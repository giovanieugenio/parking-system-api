package com.park.api;

import com.park.api.web.dto.VacancyCreateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/vacancy/vacancy-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/vacancy/vacancy-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class VacancyIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void createVacancy_WithValidData_Status201(){
        testClient
                .post()
                .uri("/api/v1/vacancy")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joel@gmail.com", "123456"))
                .bodyValue(new VacancyCreateDTO("A-05", "FREE"))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION);
    }

    @Test
    public void creatVacancy_WithCodeDuplicate_Status409(){
        testClient
                .post()
                .uri("/api/v1/vacancy")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joel@gmail.com", "123456"))
                .bodyValue(new VacancyCreateDTO("A-01", "FREE"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody()
                .jsonPath("status").isEqualTo(409)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/vacancy");
    }

    @Test
    public void creatVacancy_WithInvalidData_Status422(){
        testClient
                .post()
                .uri("/api/v1/vacancy")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joel@gmail.com", "123456"))
                .bodyValue(new VacancyCreateDTO("", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/vacancy");

        testClient
                .post()
                .uri("/api/v1/vacancy")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joel@gmail.com", "123456"))
                .bodyValue(new VacancyCreateDTO("A-01456", "NDA"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/vacancy");
    }

    @Test
    public void findVacancy_WithExistingCode_Status200(){
        testClient
                .get()
                .uri("/api/v1/vacancy/{code}", "A-01")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joel@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("id").isEqualTo(10)
                .jsonPath("code").isEqualTo("A-01")
                .jsonPath("status").isEqualTo("FREE");
    }

    @Test
    public void findVacancy_WithNonExistingCode_Status404(){
        testClient
                .get()
                .uri("/api/v1/vacancy/{code}", "A-11")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joel@gmail.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo(404)
                .jsonPath("method").isEqualTo("GET")
                .jsonPath("path").isEqualTo("/api/v1/vacancy/A-11");
    }

    @Test
    public void findVacancy_WithNotAllowedUser_Status403(){
        testClient
                .get()
                .uri("/api/v1/vacancy/{code}", "A-01")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ellie@gmail.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo(403)
                .jsonPath("method").isEqualTo("GET")
                .jsonPath("path").isEqualTo("/api/v1/vacancy/A-01");
    }

    @Test
    public void createVacancy_WithNotAllowedUser_Status403(){
        testClient
                .post()
                .uri("/api/v1/vacancy")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ellie@gmail.com", "123456"))
                .bodyValue(new VacancyCreateDTO("A-01456", "FREE"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo(403)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/vacancy");
    }
}