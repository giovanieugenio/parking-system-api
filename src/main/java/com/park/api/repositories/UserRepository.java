package com.park.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.park.api.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
