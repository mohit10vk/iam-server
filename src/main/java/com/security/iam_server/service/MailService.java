package com.security.iam_server.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

	private final JavaMailSender mailSender;

	public MailService(JavaMailSender mailSender) {
		
		this.mailSender = mailSender;
	}
	
	public void sendPasswordResetMail(String to, String token){
		
		String resetLink = "http://localhost:4200/reset-password?token=" + token;
		
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setTo(to);
		message.setSubject("Password Reset Request");
		message.setText("Click the link below to reset your password:\n\n " + resetLink);
		mailSender.send(message);
	}
}
