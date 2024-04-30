package com.park.api;

import com.park.api.web.dto.UserPasswordDTO;
import com.park.api.web.exceptions.ErrorMessage;
import jakarta.validation.constraints.Null;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.park.api.web.dto.UserCreateDTO;
import com.park.api.web.dto.UserResponseDTO;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/users-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserIT {

	@Autowired
	WebTestClient testClient;
	
	@Test
	public void createUser_WithUsernameAndPasswordValid_Status201() {
		UserResponseDTO responseBody = testClient.post()
			.uri("/api/v1/users")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(new UserCreateDTO("bill@gmail.com", "235689"))
			.exchange()
			.expectStatus().isCreated()
			.expectBody(UserResponseDTO.class)
			.returnResult().getResponseBody();
		
		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.getId()).isNotNull();
		Assertions.assertThat(responseBody.getUsername()).isEqualTo("bill@gmail.com");
		Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENT");
	}

	@Test
	public void createUser_WithIvalidUsername_ErrorMessageStatus422() {
		ErrorMessage responseBody = testClient.post()
				.uri("/api/v1/users")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UserCreateDTO("", "235689"))
				.exchange()
				.expectStatus().isEqualTo(422)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

		responseBody = testClient.post()
				.uri("/api/v1/users")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UserCreateDTO("many@", "235689"))
				.exchange()
				.expectStatus().isEqualTo(422)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

		responseBody = testClient.post()
				.uri("/api/v1/users")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UserCreateDTO("many@email", "235689"))
				.exchange()
				.expectStatus().isEqualTo(422)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
	}

	@Test
	public void createUser_WithUsernameDuplicate_Status409() {
		ErrorMessage responseBody = testClient.post()
				.uri("/api/v1/users")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UserCreateDTO("bill@gmail.com", "235689"))
				.exchange()
				.expectStatus().isEqualTo(409)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
	}

	@Test
	public void findUser_WithExistingId_Status200() {
		UserResponseDTO responseBody = testClient.get()
				.uri("/api/v1/users/100")
				.exchange()
				.expectStatus().isOk()
				.expectBody(UserResponseDTO.class)
				.returnResult().getResponseBody();

		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.getId()).isEqualTo(100);
		Assertions.assertThat(responseBody.getUsername()).isEqualTo("joel@gmail.com");
		Assertions.assertThat(responseBody.getRole()).isEqualTo("ADMIN");
	}

	@Test
	public void findUser_WithNonExistentId_Status404() {
		ErrorMessage responseBody = testClient.get()
				.uri("/api/v1/users/0")
				.exchange()
				.expectStatus().isNotFound()
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
	}

	@Test
	public void editPassword_Status204() {
		testClient.patch()
				.uri("/api/v1/users/100")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UserPasswordDTO("123456", "456789", "456789"))
				.exchange()
				.expectStatus().isNoContent();
	}

	@Test
	public void editPassword_WithNonExistentPassowrd_Status404() {
		testClient.patch()
				.uri("/api/v1/users/0")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UserPasswordDTO("123456", "456789", "456789"))
				.exchange()
				.expectStatus().isNoContent();
	}

	@Test
	public void editPassword_WithInvalidFields_Status422() {
		ErrorMessage responseBody = testClient.patch()
				.uri("/api/v1/users/100")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UserPasswordDTO("", "", ""))
				.exchange()
				.expectStatus().isEqualTo(422)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

		responseBody = testClient.patch()
				.uri("/api/v1/users/100")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UserPasswordDTO("12345", "12345", "12345"))
				.exchange()
				.expectStatus().isEqualTo(422)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

		responseBody = testClient.patch()
				.uri("/api/v1/users/100")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UserPasswordDTO("12345678", "12345678", "12345678"))
				.exchange()
				.expectStatus().isEqualTo(422)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
	}

	@Test
	public void editPassword_WithInvalidFields_Status500() {
		ErrorMessage responseBody = testClient.patch()
				.uri("/api/v1/users/100")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UserPasswordDTO("123456", "852741", "808080"))
				.exchange()
				.expectStatus().isEqualTo(500)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.getStatus()).isEqualTo(500);

		responseBody = testClient.patch()
				.uri("/api/v1/users/100")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UserPasswordDTO("808080", "123456", "123456"))
				.exchange()
				.expectStatus().isEqualTo(500)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.getStatus()).isEqualTo(500);
	}

	@Test
	public void findAllUsers_WithExistingId_Status200() {
		List<UserResponseDTO> responseBody = testClient.get()
				.uri("/api/v1/users")
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(UserResponseDTO.class)
				.returnResult().getResponseBody();

		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.size()).isEqualTo(3);

	}
}
