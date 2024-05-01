package com.park.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
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

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Transactional
	public User saveUser(User user) {
		try{
			user.setPassword(passwordEncoder.encode(user.getPassword()));
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
		if(passwordEncoder.matches(currentPassword, user.getPassword())) {
			throw new RuntimeException("password does not match the current one");
		}
		user.setPassword(passwordEncoder.encode(newPassword));
		return user;	
	}
	
	@Transactional
	public List<User> findAll(){
		return userRepository.findAll();
	}

    @Transactional
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(String.format("User '%s' not found", username)));
    }

    @Transactional
    public User.Role findRoleByUsername(String username){
        return userRepository.findRoleByUsername(username);
    }
}