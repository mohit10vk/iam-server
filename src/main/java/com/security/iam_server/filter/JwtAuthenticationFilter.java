package com.security.iam_server.filter;

import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.security.iam_server.security.JwtSecurity;
import com.security.iam_server.service.impl.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	private final JwtSecurity jwtSecurity;
	private final CustomUserDetailsService customUserDetailsService;
	
	
	public JwtAuthenticationFilter(JwtSecurity jwtSecurity, CustomUserDetailsService customUserDetailsService) {
		
		this.jwtSecurity = jwtSecurity;
		this.customUserDetailsService = customUserDetailsService;
	}


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException { 
		
		String authHeader = request.getHeader("Authorization");

		System.out.println("Authorization Header = " + authHeader);

		if (authHeader == null) {
		    System.out.println("Authorization Header NULL");
		    filterChain.doFilter(request, response);
		    return;
		}

		if (!authHeader.startsWith("Bearer ")) {
		    System.out.println("Bearer Missing");
		    filterChain.doFilter(request, response);
		    return;
		}
		
		String token = authHeader.substring(7);
		System.out.print("Token = " + token);
		
		String email = jwtSecurity.extractUsername(token);
		System.out.print("EMAIL = " + email);
		
		System.out.println("Role From JWT = " + jwtSecurity.extractRole(token));

		
		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

		    UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

		    System.out.println("Authorities = " + userDetails.getAuthorities());

		    if (jwtSecurity.isTokenValid(token, userDetails.getUsername())) {

		        System.out.println("TOKEN VALID");

		        UsernamePasswordAuthenticationToken authToken =
		                new UsernamePasswordAuthenticationToken(
		                        userDetails,
		                        null,
		                        userDetails.getAuthorities());

		        authToken.setDetails(
		                new WebAuthenticationDetailsSource().buildDetails(request));

		        SecurityContextHolder.getContext().setAuthentication(authToken);

		        System.out.println("After Set = "
		                + SecurityContextHolder.getContext().getAuthentication());
		    }
		}

		System.out.println("Before Next Filter = "
		        + SecurityContextHolder.getContext().getAuthentication());

		filterChain.doFilter(request, response);
	
	
	
	}

}
