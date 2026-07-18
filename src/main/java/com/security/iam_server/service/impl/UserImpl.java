package com.security.iam_server.service.impl;

import java.util.Optional;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.security.iam_server.dto.ChangePasswordRequest;
import com.security.iam_server.entity.User;
import com.security.iam_server.enums.Role;
import com.security.iam_server.exception.BadRequestException;
import com.security.iam_server.repository.UserRepository;
import com.security.iam_server.service.RefreshTokenService;
import com.security.iam_server.service.UserService;

@Service
public class UserImpl implements UserService{

	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	private final RefreshTokenService refreshTokenService;
	
	public UserImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
			RefreshTokenService refreshTokenService) {
		
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.refreshTokenService = refreshTokenService;
	}

	@Override
	public String saveUser(User user) {
		if(userRepository.existsFindByEmail(user.getEmail())) {
			return "Email already exists";
		}
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		user.setRole(Role.ROLE_USER);
	
		userRepository.save(user);
		
		return "SUCCESSFULLY CREATED";
	}

	@Override
	public Optional<User> findByEmail(String email) {
		 Optional<User> user = userRepository.findByEmail(email);
		if (user.isEmpty()) {
	        throw new RuntimeException("User not found");
	    }
		return user;
	}

	@Override
	public Boolean existsFindByEmail(String email) {
		return userRepository.existsFindByEmail(email);
	}

	@Override
	public String updateUser(Long id, User user) {
		
		User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
		
		existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setPassword(user.getPassword());

        userRepository.save(existingUser);

		return "User Update Successfully";
	}

	@Override
	public String deleteUser(Long id) {
		
		User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
		
		 userRepository.delete(existingUser);
		 
		return "User Deleted Successfully";
	}

	@Override
	public void logout(String email) {
		
		User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User Not Found"));

	    refreshTokenService.logout(user);
	}

	@Override
	public String changePassword(String email, ChangePasswordRequest request) {

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new BadRequestException("User not found"));

	    if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
	        throw new BadRequestException("Old Password is incorrect");
	    }

	    if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
	        throw new BadRequestException("New password must be different from old password");
	    }

	    if (!request.getNewPassword().equals(request.getConfirmPassword())) {
	        throw new BadRequestException("New Password and Confirm Password do not match");
	    }

	    user.setPassword(passwordEncoder.encode(request.getNewPassword()));

	    userRepository.save(user);

	    return "Password Changed Successfully";
	}

	

}
















