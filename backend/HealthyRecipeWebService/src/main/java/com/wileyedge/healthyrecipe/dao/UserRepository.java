package com.wileyedge.healthyrecipe.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wileyedge.healthyrecipe.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	@Query("SELECT u FROM User u WHERE u.email = :email")
    User findByEmail(String email);
	
	@Query("SELECT u FROM User u WHERE u.username = :username")
    User findByUsername(String username);
	
	boolean existsByEmail(String email);
	
	boolean existsByUsername(String username);
	
	
}
