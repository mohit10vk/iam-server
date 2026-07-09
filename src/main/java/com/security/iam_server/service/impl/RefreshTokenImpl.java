package com.security.iam_server.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.security.iam_server.entity.RefreshToken;
import com.security.iam_server.entity.User;
import com.security.iam_server.repository.RefreshTokenRepository;
import com.security.iam_server.service.RefreshTokenService;

@Service
public class RefreshTokenImpl implements RefreshTokenService{

	private final RefreshTokenRepository refreshTokenRepository;

	public RefreshTokenImpl(RefreshTokenRepository refreshTokenRepository) {
		this.refreshTokenRepository = refreshTokenRepository;
	}

	@Override
	public RefreshToken createRefreshToken(User user) {
		
		refreshTokenRepository.findByUser(user)
        .ifPresent(refreshTokenRepository::delete);
		
		 RefreshToken refreshToken = new RefreshToken();

		    refreshToken.setUser(user);
		    refreshToken.setToken(UUID.randomUUID().toString());
		    refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));
		    
		return refreshTokenRepository.save(refreshToken);
	}

	@Override
	public Optional<RefreshToken> findByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}

	@Override
	public boolean verfiyExpiration(RefreshToken token) {
		return token.getExpiryDate().isAfter(LocalDateTime.now());
	}

	@Override
	public void deleteByUser(User user) {
		refreshTokenRepository.deleteByUser(user);
	}
	
	
	
	

}
