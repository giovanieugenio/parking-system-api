package com.park.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.park.api.entities.User;
import com.park.api.exception.EntityNotFoundException;
import com.park.api.exception.UsernameUniqueViolationException;
import com.park.api.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor  //variable userRepository
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	public User salvar(User user) {
		try{
			return userRepository.save(user);
		} catch (DataIntegrityViolationException e) {
			throw new UsernameUniqueViolationException(String.format("Username '%s' already exists", user.getUsername()));
		}
	}
	
	@Transactional
	public User findById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("User 'id='%s''not found", id)));
	}
	
	@Transactional
	public User editPassword(Long id, String currentPassword, String newPassword, String confirmPassword) {
		if(!newPassword.equals(confirmPassword)) {
			throw new RuntimeException("new password different from confirmation");
		}
		User user = userRepository.findById(id).get();
		if(!user.getPassword().equals(currentPassword)) {
			throw new RuntimeException("password does not match the current one");
		}
		user.setPassword(newPassword);
		return user;	
	}
	
	@Transactional
	public List<User> findAll(){
		return userRepository.findAll();
	}
}