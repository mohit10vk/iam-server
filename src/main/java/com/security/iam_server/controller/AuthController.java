package com.security.iam_server.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.iam_server.dto.ForgotPasswordRequest;
import com.security.iam_server.dto.LoginRequest;
import com.security.iam_server.dto.LoginResponse;
import com.security.iam_server.dto.RefreshTokenRequest;
import com.security.iam_server.dto.ResetPasswordRequest;
import com.security.iam_server.service.AuthService;
import com.security.iam_server.service.UserService;

@RestController
@RequestMapping("/user")
public class AuthController {

	private final UserService userService;
	private final AuthService authService;

	
	
	public AuthController(UserService userService, AuthService authService) {
		super();
		this.userService = userService;
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
	
	@GetMapping("/profile")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String profile() {
        return "Welcome User";
    }
	
	@PostMapping("/logout")
    public ResponseEntity<String> logout(Authentication authentication) {

        String email = authentication.getName();

        userService.logout(email);

        return ResponseEntity.ok("Logout Successfully");
    }
	
	@PostMapping("/forgot-password")
	public ResponseEntity<String> forgotPassword(
	        @RequestBody ForgotPasswordRequest request) {

	    return ResponseEntity.ok(authService.forgotPassword(request));
	}
	
	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(
	        @RequestBody ResetPasswordRequest request) {

	    return ResponseEntity.ok(authService.resetPassword(request));
	}
	
}
