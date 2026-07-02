package com.security.iam_server.service;

import java.util.Optional;

import com.security.iam_server.entity.User;

public interface UserService {
	
	String saveUser(User user);
	
	Optional<User> findByEmail(String email);
	
	Boolean existsFindByEmail(String email);
	
	String updateUser(Long id, User user);
	
	String deleteUser(Long id);
}
