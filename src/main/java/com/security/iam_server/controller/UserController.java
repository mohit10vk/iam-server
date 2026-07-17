package com.security.iam_server.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.iam_server.entity.User;
import com.security.iam_server.service.UserService;


@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/register")
	String saveUser(@RequestBody User user) {
		return userService.saveUser(user);
	}
	
	@GetMapping("/email/{email}")
	Optional<User> findByEmail(@PathVariable String email){
		return userService.findByEmail(email);
	}
	
     @GetMapping("/exists/{email}")
	    public Boolean existsFindByEmail(@PathVariable String email) {
	        return userService.existsFindByEmail(email);
	}
     
     @PutMapping("/update/{id}")
     String updateUser(@PathVariable Long id,@RequestBody User user){
    	 return userService.updateUser(id, user);
     }
     
     @DeleteMapping("/delete/{id}")
     String deleteUser(@PathVariable Long id){
    	 return userService.deleteUser(id);
     }
     
     
     
}









