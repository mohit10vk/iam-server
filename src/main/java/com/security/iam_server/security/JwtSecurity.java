package com.security.iam_server.security;


import java.util.Date;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.security.iam_server.entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtSecurity {

	@Value("${jwt.secret}")
	private String secretKey;
	
	private SecretKey getSignInKey() {
		
		
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
	
	public String generateToken(User user) {

	    return Jwts.builder()
	            .subject(user.getEmail()).claim("role", user.getRole().name())
	            .issuedAt(new Date())
	            .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
	            .signWith(getSignInKey())
	            .compact();
	}
	
	public String extractUsername(String token) {
	    return Jwts.parser()
	            .verifyWith(getSignInKey())
	            .build()
	            .parseSignedClaims(token)
	            .getPayload()
	            .getSubject();
	}
	
	public String extractRole(String token) {

	    return Jwts.parser()
	            .verifyWith(getSignInKey())
	            .build()
	            .parseSignedClaims(token)
	            .getPayload()
	            .get("role", String.class);
	}

	public boolean isTokenValid(String token, String email) {
	    return extractUsername(token).equals(email);
	}
	
	
	
	
	
}
