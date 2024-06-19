package com.park.api;

import com.park.api.web.dto.PageableDTO;
import com.park.api.web.dto.ParkingCreateDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/parking/parking-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/parking/parking-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ParkingIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void createCheckIn_WithValidData_CreatedAndLocation() {
        ParkingCreateDTO createDTO = ParkingCreateDTO.builder()
                .plate("WER-1111")
                .brand("FIAT")
                .model("PALIO 1.0")
                .color("AZUL")
                .clientCpf("42104344000")
                .build();

        testClient.post().uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joel@gmail.com", "123456"))
                .bodyValue(createDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectBody()
                .jsonPath("plate").isEqualTo("WER-1111")
                .jsonPath("brand").isEqualTo("FIAT")
                .jsonPath("model").isEqualTo("PALIO 1.0")
                .jsonPath("color").isEqualTo("AZUL")
                .jsonPath("clientCpf").isEqualTo("42104344000")
                .jsonPath("receipt").exists()
                .jsonPath("entryDate").exists()
                .jsonPath("code").exists();
    }

    @Test
    public void createCheckIn_WithClientRole_Status403() {
        ParkingCreateDTO createDTO = ParkingCreateDTO.builder()
                .plate("WER-1111")
                .brand("FIAT")
                .model("PALIO 1.0")
                .color("AZUL")
                .clientCpf("42104344000")
                .build();

        testClient.post().uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "elie@gmail.com", "123456"))
                .bodyValue(createDTO)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo("403")
                .jsonPath("path").isEqualTo("/api/v1/parking/check-in")
                .jsonPath("method").isEqualTo("POST");
    }

    @Test
    public void createCheckIn_WithInvalidData_Status422() {
        ParkingCreateDTO createDTO = ParkingCreateDTO.builder()
                .plate("")
                .brand("")
                .model("")
                .color("")
                .clientCpf("")
                .build();

        testClient.post().uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joel@gmail.com", "123456"))
                .bodyValue(createDTO)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo("422")
                .jsonPath("path").isEqualTo("/api/v1/parking/check-in")
                .jsonPath("method").isEqualTo("POST");
    }

    @Test
    public void createCheckIn_WithNonExistingCpf_Status404() {
        ParkingCreateDTO createDTO = ParkingCreateDTO.builder()
                .plate("WER-1111")
                .brand("FIAT")
                .model("PALIO 1.0")
                .color("AZUL")
                .clientCpf("05608655036")
                .build();

        testClient.post().uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joel@gmail.com", "123456"))
                .bodyValue(createDTO)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/parking/check-in")
                .jsonPath("method").isEqualTo("POST");
    }

    @Sql(scripts = "/sql/parking/parking-insert-vacancy-occupied.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/parking/parking-delete-vacancy-occupied.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void createCheckIn_WithOccupiedVacancy_Status404() {
        ParkingCreateDTO createDTO = ParkingCreateDTO.builder()
                .plate("WER-1111")
                .brand("FIAT")
                .model("PALIO 1.0")
                .color("AZUL")
                .clientCpf("2104344000")
                .build();

        testClient.post().uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joel@gmail.com", "123456"))
                .bodyValue(createDTO)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/parking/check-in")
                .jsonPath("method").isEqualTo("POST");
    }

    @Test
    public void findCheckIn_WithAdminProfile_Status200() {
        testClient.get().uri("/api/v1/parking/check-in/{receipt}", "20230313-101300")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joel@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("plate").isEqualTo("FIT-1020")
                .jsonPath("brand").isEqualTo("FIAT")
                .jsonPath("model").isEqualTo("PALIO")
                .jsonPath("color").isEqualTo("VERDE")
                .jsonPath("clientCpf").isEqualTo("66484519048")
                .jsonPath("receipt").isEqualTo("20230313-101300")
                .jsonPath("entryDate").isEqualTo("2023-03-13 10:15:00")
                .jsonPath("code").isEqualTo("A-01");
    }

    @Test
    public void findCheckIn_WithClientProfile_Status200() {
        testClient.get().uri("/api/v1/parking/check-in/{receipt}", "20230313-101300")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ellie@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("plate").isEqualTo("FIT-1020")
                .jsonPath("brand").isEqualTo("FIAT")
                .jsonPath("model").isEqualTo("PALIO")
                .jsonPath("color").isEqualTo("VERDE")
                .jsonPath("clientCpf").isEqualTo("66484519048")
                .jsonPath("receipt").isEqualTo("20230313-101300")
                .jsonPath("entryDate").isEqualTo("2023-03-13 10:15:00")
                .jsonPath("code").isEqualTo("A-01");
    }

    @Test
    public void findCheckIn_WithNonExistingReceipt_Status404() {
        testClient.get().uri("/api/v1/parking/check-in/{receipt}", "20230313-777300")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ellie@gmail.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/parking/check-in/20230313-777300")
                .jsonPath("method").isEqualTo("GET");
    }

    @Test
    public void createCheckOut_WithExistingReceipt_Status200() {
        testClient.get().uri("/api/v1/parking/check-in/{receipt}", "20230313-101300")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joel@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("plate").isEqualTo("FIT-1020")
                .jsonPath("brand").isEqualTo("FIAT")
                .jsonPath("model").isEqualTo("PALIO")
                .jsonPath("color").isEqualTo("VERDE")
                .jsonPath("clientCpf").isEqualTo("66484519048")
                .jsonPath("receipt").isEqualTo("20230313-101300")
                .jsonPath("entryDate").exists()
                .jsonPath("exitDate").exists()
                .jsonPath("code").isEqualTo("A-01")
                .jsonPath("price").exists()
                .jsonPath("discount").exists();
    }

    @Test
    public void createCheckOut_WithNonExistingReceipt_Status404() {
        testClient.put().uri("/api/v1/parking/check-in/{receipt}", "00000013-00001300")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joel@gmail.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/parking/check-in/00000013-00001300")
                .jsonPath("method").isEqualTo("PUT");
    }

    @Test
    public void createCheckOut_WithClientRole_Status403() {
        testClient.put().uri("/api/v1/parking/check-in/{receipt}", "20230313-101300")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ellie@gmail.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo("403")
                .jsonPath("path").isEqualTo("/api/v1/parking/check-in/20230313-101300")
                .jsonPath("method").isEqualTo("PUT");
    }

    @Test
    public void findParking_WithClientCpf_Status200() {
        PageableDTO responseBody = testClient.get().uri("/api/v1/parking/cpf/{cpf}?size=1&page=0", "66484519048")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joel@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDTO.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1);
        Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2);
        Assertions.assertThat(responseBody.getNumber()).isEqualTo(0);
        Assertions.assertThat(responseBody.getSize()).isEqualTo(1);

        testClient.get().uri("/api/v1/parking/cpf/{cpf}?size=1&page=1", "66484519048")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joel@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDTO.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1);
        Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2);
        Assertions.assertThat(responseBody.getNumber()).isEqualTo(1);
        Assertions.assertThat(responseBody.getSize()).isEqualTo(1);
    }

    @Test
    public void findParking_WithClientCpfRole_Status403() {
        testClient.get().uri("/api/v1/parking/cpf/{cpf}", "42104344000")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ellie@gmail.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo("403")
                .jsonPath("path").isEqualTo("/api/v1/parking/cpf/42104344000")
                .jsonPath("method").isEqualTo("GET");
    }

    @Test
    public void findParking_WithClientLoggedIn_Status200() {
        PageableDTO responseBody = testClient.get().uri("/api/v1/parking?size=1&page=0")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "abby@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDTO.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1);
        Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2);
        Assertions.assertThat(responseBody.getNumber()).isEqualTo(0);
        Assertions.assertThat(responseBody.getSize()).isEqualTo(1);

        testClient.get().uri("/api/v1/parking?size=1&page=1")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "abby@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDTO.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1);
        Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2);
        Assertions.assertThat(responseBody.getNumber()).isEqualTo(1);
        Assertions.assertThat(responseBody.getSize()).isEqualTo(1);
    }

    @Test
    public void findParking_WithAdminRole_FindClientLoggedIn_Status403() {
        testClient.get().uri("/api/v1/parking")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joel@gmail.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo("403")
                .jsonPath("path").isEqualTo("/api/v1/parking")
                .jsonPath("method").isEqualTo("GET");
    }
}
