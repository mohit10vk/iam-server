package com.security.iam_server.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.security.iam_server.entity.User;
import com.security.iam_server.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	
	private UserRepository userRepository;
	
	

	public CustomUserDetailsService(UserRepository userRepository) {
		
		this.userRepository = userRepository;
	}



	@Override
	public UserDetails loadUserByUsername(String email)
			throws UsernameNotFoundException {
		
		User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));
		
		return org.springframework.security.core.userdetails.User
				.builder()
				.username(user.getEmail())
				.password(user.getPassword())
				.roles(user.getRole().name().replace("ROLE_", ""))
				.build();
	}

}
