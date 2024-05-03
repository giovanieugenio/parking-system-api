package com.park.api.web.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.park.api.entities.User;
import com.park.api.service.UserService;
import com.park.api.web.dto.UserCreateDTO;
import com.park.api.web.dto.UserPasswordDTO;
import com.park.api.web.dto.UserResponseDTO;
import com.park.api.web.dto.mapper.UserMapper;
import com.park.api.web.exceptions.ErrorMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Users", description = "Contains all operations related to resources for registering, editing and reading users")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Operation(summary = "Create a new user", responses = {
			@ApiResponse(responseCode = "201", 
					description = "resource created successfully",
					content = @Content(mediaType = "application/json", 
					schema = @Schema(implementation = UserResponseDTO.class))),
			@ApiResponse(responseCode = "409", 
					description = "username already exists!",
					content = @Content(mediaType = "application/json", 
					schema = @Schema(implementation = ErrorMessage.class))),
			@ApiResponse(responseCode = "422", 
					description = "invalid input data",
					content = @Content(mediaType = "application/json", 
					schema = @Schema(implementation = ErrorMessage.class))),
	})
	@PostMapping
	public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserCreateDTO userDTO){
		User newUser = userService.saveUser(UserMapper.toUser(userDTO));
		return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDTO(newUser));
	}
	
	@Operation(summary = "Retrieve a user by id", description = "The request requires a bearer token. Access restricted to Admin or Customer", responses = {
			@ApiResponse(responseCode = "200", 
					description = "Resource retrieved successfully!",
					content = @Content(mediaType = "application/json", 
					schema = @Schema(implementation = UserResponseDTO.class))),
			@ApiResponse(responseCode = "404", 
					description = "Resource not found.",
					content = @Content(mediaType = "application/json", 
					schema = @Schema(implementation = ErrorMessage.class))),
			@ApiResponse(responseCode = "403",
					description = "User is not allowed to access this resource.",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorMessage.class))),
	})
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN') OR (hasRole('CLIENT') AND #id == authentication.principal.id)")
	public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id){
		User find = userService.findById(id);
		return ResponseEntity.ok().body(UserMapper.toDTO(find));
	}
	
	@Operation(summary = "Update password", security = @SecurityRequirement(name = "security"),
			description = "The request requires a bearer token. Access restricted to Admin or Customer",
			responses = {
			@ApiResponse(responseCode = "204", 
					description = "Resource updated successfully!"),
			@ApiResponse(responseCode = "400", 
					description = "invalid password!",
					content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = ErrorMessage.class))),
			@ApiResponse(responseCode = "422",
					description = "invalid fields",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorMessage.class))),
			@ApiResponse(responseCode = "403",
					description = "User is not allowed to access this resource.",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorMessage.class))),
	})
	@PatchMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENT') AND (#id == authentication.principal.id)")
	public ResponseEntity<Void> updatePassword(@PathVariable Long id, @Valid @RequestBody UserPasswordDTO dto){
		userService.editPassword(id, dto.getCurrentPassword(), dto.getNewPassword(), dto.getConfirmPassword());
		return ResponseEntity.noContent().build();
	}
	
	@Operation(summary = "Retrieve all users", security = @SecurityRequirement(name = "security"), description = "Access restricted to Admin",responses = {
			@ApiResponse(responseCode = "200", description = "All registered users",
					content = @Content(mediaType = "application/json",
					array = @ArraySchema(schema = @Schema(implementation = UserResponseDTO.class)))),
			@ApiResponse(responseCode = "403", description = "User is not allowed to access this resource.",
							content = @Content(mediaType = "application/json",
									schema = @Schema(implementation = ErrorMessage.class))),
	})
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<UserResponseDTO>> getAll(){
		List<User> list = userService.findAll();
		return ResponseEntity.ok(UserMapper.toListDTO(list));
	}
}
