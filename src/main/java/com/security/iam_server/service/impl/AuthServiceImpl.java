package com.security.iam_server.service.impl;


import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.security.iam_server.dto.ForgotPasswordRequest;
import com.security.iam_server.dto.LoginRequest;
import com.security.iam_server.dto.LoginResponse;
import com.security.iam_server.dto.RefreshTokenRequest;
import com.security.iam_server.dto.ResetPasswordRequest;
import com.security.iam_server.entity.PasswordResetToken;
import com.security.iam_server.entity.RefreshToken;
import com.security.iam_server.entity.User;
import com.security.iam_server.exception.BadRequestException;
import com.security.iam_server.repository.PasswordResetTokenRepository;
import com.security.iam_server.repository.UserRepository;
import com.security.iam_server.security.JwtSecurity;
import com.security.iam_server.service.AuthService;
import com.security.iam_server.service.MailService;
import com.security.iam_server.service.RefreshTokenService;


@Service
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    private final PasswordResetTokenRepository passwordResetTokenRepository;
	
	private final AuthenticationManager authenticationManager;
	
	private final JwtSecurity jwtSecurity;
	
	private final UserRepository userRepository;
	
	private final RefreshTokenService refreshTokenService;
	
	

	public AuthServiceImpl(PasswordEncoder passwordEncoder, MailService mailService,
			PasswordResetTokenRepository passwordResetTokenRepository, AuthenticationManager authenticationManager,
			JwtSecurity jwtSecurity, UserRepository userRepository, RefreshTokenService refreshTokenService) {
		
		this.passwordEncoder = passwordEncoder;
		this.mailService = mailService;
		this.passwordResetTokenRepository = passwordResetTokenRepository;
		this.authenticationManager = authenticationManager;
		this.jwtSecurity = jwtSecurity;
		this.userRepository = userRepository;
		this.refreshTokenService = refreshTokenService;
	}


	@Override
	public LoginResponse login(LoginRequest loginRequest) {
		
		authenticationManager.authenticate(
			    new UsernamePasswordAuthenticationToken(
			        loginRequest.getEmail(),
			        loginRequest.getPassword()));
		
		 User user = userRepository.findByEmail(loginRequest.getEmail())
	                .orElseThrow(() -> new RuntimeException("User not found"));
		 
		 String accessToken = jwtSecurity.generateToken(user);
		 
		 RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);


			return new LoginResponse(
	                accessToken,
	                refreshToken.getToken(),
	                "Bearer",
	                3600);
	}


	@Override
	public LoginResponse refreshToken(RefreshTokenRequest request) {
		
		RefreshToken refreshToken = refreshTokenService
	            .findByToken(request.getRefreshToken())
	            .orElseThrow(() ->
	                    new RuntimeException("Refresh Token Not Found"));

	    if (!refreshTokenService.verfiyExpiration(refreshToken)) {
	        throw new RuntimeException("Refresh Token Expired");
	    }

	    User user = refreshToken.getUser();

	    String accessToken = jwtSecurity.generateToken(user);

	    return new LoginResponse(
	            accessToken,
	            refreshToken.getToken(),
	            "Bearer",
	            3600);
	}


	@Override
	@Transactional
	public String forgotPassword(ForgotPasswordRequest request) {
		User user = userRepository.findByEmail(request.getEmail())
		        .orElseThrow(() -> new BadRequestException("User not found"));

		passwordResetTokenRepository.deleteByUserId(user.getId());

		String token = UUID.randomUUID().toString();

		PasswordResetToken resetToken = new PasswordResetToken();

		resetToken.setToken(token);
		resetToken.setUser(user);
		resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));

		passwordResetTokenRepository.save(resetToken);

		mailService.sendPasswordResetMail(user.getEmail(), token);

		return "Password reset link sent successfully";
	}


	@Override
	@Transactional
	public String resetPassword(ResetPasswordRequest request) {
		PasswordResetToken resetToken = passwordResetTokenRepository
	            .findByToken(request.getToken())
	            .orElseThrow(() -> new BadRequestException("Invalid token"));

	    if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
	        throw new BadRequestException("Token has expired");
	    }

	    if (!request.getNewPassword().equals(request.getConfirmPassword())) {
	        throw new BadRequestException("New Password and Confirm Password do not match");
	    }

	    User user = resetToken.getUser();

	    if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
	        throw new BadRequestException("New password must be different from old password");
	    }

	    user.setPassword(passwordEncoder.encode(request.getNewPassword()));

	    userRepository.save(user);

	    passwordResetTokenRepository.delete(resetToken);

	    return "Password reset successfully";
	}


	


	
	
	
	
	

}
