package com.security.iam_server.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.security.iam_server.entity.RefreshToken;
import com.security.iam_server.entity.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{

	 Optional<RefreshToken> findByToken(String token);
	
	 Optional<RefreshToken> findByUser(User user);
	
	 @Transactional
	 void deleteByUser(User user);
	 
	
}
