package com.security.iam_server.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.security.iam_server.entity.User;
import com.security.iam_server.repository.UserRepository;
import com.security.iam_server.service.UserService;

@Service
public class UserImpl implements UserService{

   
	
	@Autowired
	private UserRepository userRepository;

  
	@Override
	public String saveUser(User user) {
		if(userRepository.existsFindByEmail(user.getEmail())) {
			return "Email already exists";
		}
			
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
        existingUser.setPasswords(user.getPasswords());

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

	
	
	

}
