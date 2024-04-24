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

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@PostMapping
	public ResponseEntity<UserResponseDTO> create(@RequestBody UserCreateDTO userDTO){
		User newUser = userService.salvar(UserMapper.toUser(userDTO));
		return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDTO(newUser));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id){
		User find = userService.findById(id);
		return ResponseEntity.ok().body(UserMapper.toDTO(find));
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody UserPasswordDTO dto){
		User up = userService.editPassword(id, dto.getCurrentPassword(), dto.getNewPassword(), dto.getConfirmPassword());
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping
	public ResponseEntity<List<UserResponseDTO>> getAll(){
		List<User> list = userService.findAll();
		return ResponseEntity.ok(UserMapper.toListDTO(list));
	}
}
