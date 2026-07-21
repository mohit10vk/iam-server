package com.security.iam_server.config;


import com.security.iam_server.filter.JwtAuthenticationFilter;
import com.security.iam_server.service.impl.CustomUserDetailsService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final CustomUserDetailsService customUserDetailsService;
	
	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
			CustomUserDetailsService customUserDetailsService) {
		
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
		this.customUserDetailsService = customUserDetailsService;
	}

	@Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

	        http
	            .csrf(csrf -> csrf.disable())
	            .authorizeHttpRequests(auth -> auth
	                .requestMatchers("/user/register","/user/login",
	                        "/user/refresh-token",
	                        "/user/forgot-password",
	                        "/user/reset-password").permitAll()
	                .anyRequest().authenticated())
	            .sessionManagement(session ->
	                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	            
	            .exceptionHandling(exception -> exception
	            	    .authenticationEntryPoint((request, response, authException) -> {
	            	        System.out.println("=== AUTHENTICATION ENTRY POINT ===");
	            	        authException.printStackTrace();
	            	        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
	            	    })
	            	    .accessDeniedHandler((request, response, accessDeniedException) -> {
	            	        System.out.println("=== ACCESS DENIED HANDLER ===");
	            	        accessDeniedException.printStackTrace();
	            	        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
	            	    })
	            	)
	            
	            .authenticationProvider(authenticationProvider())
	            .addFilterBefore(jwtAuthenticationFilter,
	                    UsernamePasswordAuthenticationFilter.class)
			/* .httpBasic(Customizer.withDefaults()) */;

	        return http.build();
	    }

	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	    
	    @Bean
	    public AuthenticationProvider authenticationProvider() {

	        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

	        provider.setUserDetailsService(customUserDetailsService);
	        provider.setPasswordEncoder(passwordEncoder());

	        return provider;
	    }
	    
	    @Bean
	    public AuthenticationManager authenticationManager(
	            AuthenticationConfiguration configuration) throws Exception {

	        return configuration.getAuthenticationManager();
	    }

}
