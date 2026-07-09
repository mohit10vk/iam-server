package com.security.iam_server.service;

import java.util.Optional;

import com.security.iam_server.entity.RefreshToken;
import com.security.iam_server.entity.User;

public interface RefreshTokenService {
	
	RefreshToken createRefreshToken(User user);
	
	Optional<RefreshToken> findByToken(String token);
	
	boolean verfiyExpiration(RefreshToken token);
	
	void deleteByUser(User user);

}
