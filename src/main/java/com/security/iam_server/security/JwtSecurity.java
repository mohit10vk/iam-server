package com.security.iam_server.security;


import java.util.Date;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtSecurity {

	@Value("${jwt.secret}")
	private String secretKey;
	
	private SecretKey getSignInKey() {
		
		System.out.println(secretKey);
		System.out.println(secretKey.length());
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
	
	public String generateToken(String email) {

	    return Jwts.builder()
	            .subject(email)
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

	public boolean isTokenValid(String token, String email) {
	    return extractUsername(token).equals(email);
	}
	
	
	
	
	
}
