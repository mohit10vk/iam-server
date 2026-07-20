package com.security.iam_server.service;

import com.security.iam_server.dto.ForgotPasswordRequest;
import com.security.iam_server.dto.LoginRequest;
import com.security.iam_server.dto.LoginResponse;
import com.security.iam_server.dto.RefreshTokenRequest;
import com.security.iam_server.dto.ResetPasswordRequest;

public interface AuthService {
     
	LoginResponse login(LoginRequest loginRequest);
	
	LoginResponse refreshToken(RefreshTokenRequest request);
	
	String forgotPassword(ForgotPasswordRequest request);

	String resetPassword(ResetPasswordRequest  request);
}
