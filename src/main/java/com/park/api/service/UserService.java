package com.park.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.park.api.entities.User;
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
		return userRepository.save(user);
	}
	
	@Transactional
	public User findById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
	}
	
	@Transactional
	public User editPassword(Long id, String password) {
		User user = userRepository.findById(id).get();
		user.setPassword(password);
		return user;	
	}
	
	@Transactional
	public List<User> findAll(){
		return userRepository.findAll();
	}
}