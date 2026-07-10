package com.security.iam_server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.iam_server.dto.LoginRequest;
import com.security.iam_server.dto.LoginResponse;
import com.security.iam_server.dto.RefreshTokenRequest;
import com.security.iam_server.service.AuthService;

@RestController
@RequestMapping("/user")
public class AuthController {

	
	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

		LoginResponse response = authService.login(loginRequest);

        return ResponseEntity.ok(response);
    }
	
	@PostMapping("/refresh-token")
	public ResponseEntity<LoginResponse> refreshToken(
	        @RequestBody RefreshTokenRequest request) {

	    LoginResponse response = authService.refreshToken(request);

	    return ResponseEntity.ok(response);
	}
	
}
