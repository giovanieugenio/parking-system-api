package com.park.api.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Users", description = "Contains all operations related to resources for registering, editing and reading users")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/users")
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
		User newUser = userService.salvar(UserMapper.toUser(userDTO));
		return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDTO(newUser));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id){
		User find = userService.findById(id);
		return ResponseEntity.ok().body(UserMapper.toDTO(find));
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<Void> updatePassword(@PathVariable Long id, @Valid @RequestBody UserPasswordDTO dto){
		User up = userService.editPassword(id, dto.getCurrentPassword(), dto.getNewPassword(), dto.getConfirmPassword());
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping
	public ResponseEntity<List<UserResponseDTO>> getAll(){
		List<User> list = userService.findAll();
		return ResponseEntity.ok(UserMapper.toListDTO(list));
	}
}
