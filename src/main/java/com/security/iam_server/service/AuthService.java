package com.security.iam_server.service;

import com.security.iam_server.dto.LoginRequest;

public interface AuthService {
     
	String login(LoginRequest loginRequest);
}
