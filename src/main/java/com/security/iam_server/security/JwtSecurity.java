package com.security.iam_server.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtSecurity {

	@Value("${jwt.secret}")
	private String secretKey;
	
	private Key getSignInKey() {
		
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
}
