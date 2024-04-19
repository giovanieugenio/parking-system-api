package com.park.api.service;

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
	UserRepository userRepository;
	
	@Transactional
	public User salvar(User user) {
		return userRepository.save(user);
	}
}
