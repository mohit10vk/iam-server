package com.security.iam_server.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.security.iam_server.dto.LoginRequest;
import com.security.iam_server.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {
	
	private final AuthenticationManager authenticationManager;
	
	public AuthServiceImpl(AuthenticationManager authenticationManager) {
	
		this.authenticationManager = authenticationManager;
	}



	@Override
	public String login(LoginRequest loginRequest) {
		authenticationManager.authenticate(
			    new UsernamePasswordAuthenticationToken(
			        loginRequest.getEmail(),
			        loginRequest.getPassword()
			    )
			);

			return "Login Successful";
	}

}
