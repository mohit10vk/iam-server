package com.security.iam_server.service;

import com.security.iam_server.dto.LoginRequest;
import com.security.iam_server.dto.LoginResponse;
import com.security.iam_server.dto.RefreshTokenRequest;

public interface AuthService {
     
	LoginResponse login(LoginRequest loginRequest);
	
	LoginResponse refreshToken(RefreshTokenRequest request);
}
