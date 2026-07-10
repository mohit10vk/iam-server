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

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
		    filterChain.doFilter(request, response);
		    return;
		}
		
		String token = authHeader.substring(7);
		System.out.print("Token = " + token);
		
		String email = jwtSecurity.extractUsername(token);
		System.out.print("EMAIL = " + email);
		
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
		
		
		if (jwtSecurity.isTokenValid(token, userDetails.getUsername())) {
			
			 System.out.println("TOKEN VALID");

			  UsernamePasswordAuthenticationToken authToken =
	                    new UsernamePasswordAuthenticationToken(
	                            userDetails,
	                            null,
	                            userDetails.getAuthorities());
			  authToken.setDetails(
	                    new WebAuthenticationDetailsSource().buildDetails(request)
	            );

	            SecurityContextHolder.getContext().setAuthentication(authToken);
	            System.out.println(SecurityContextHolder.getContext().getAuthentication());
		}
		
		filterChain.doFilter(request, response);
	}
	
	
	
	
	

}
