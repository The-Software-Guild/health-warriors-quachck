package com.wileyedge.healthyrecipe.controller;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wileyedge.healthyrecipe.exception.UserNotFoundException;
import com.wileyedge.healthyrecipe.model.User;
import com.wileyedge.healthyrecipe.service.IMemberService;


@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

	private IMemberService memberService;

	@Autowired
	public UserController(IMemberService memberService) {
		this.memberService = memberService;
	}
	
	@PostConstruct
	public void createDefaultAdmin() {
	    if (!memberService.existsByEmail("admin")) {
	        User defaultAdmin = new User();
	        defaultAdmin.setUsername("admin");
	        defaultAdmin.setEmail("admin@example.com");
	        defaultAdmin.setFirstName("Admin");
	        defaultAdmin.setPassword("Admin@123");
	        defaultAdmin.setRole("ADMIN");
	        memberService.createUser(defaultAdmin);
	    }

	    if (!memberService.existsByUsername("exampleUser")) {
	        User user = new User();
	        user.setUsername("exampleUser");
	        user.setEmail("user@example.com");
	        user.setPassword("Password@123");
	        user.setFirstName("Rafael");
	        user.setLastName("Dawson");
	        user.setRole("MEMBER");
	        memberService.createUser(user);
	    }
	}

	
	@PostMapping("/signup")
	public User createUser(@RequestBody User user) {
		User createdUser = memberService.createUser(user);
		return createdUser;
	}

	
	@GetMapping("/{userId}")
	public User getUserById(@PathVariable long userId, @RequestHeader("Authorization") String token) {
		User updatedUser = memberService.findUserById(userId, token);
		return updatedUser;
	}
	

	@GetMapping("/email/{email}")
	public User getUserByEmail(@PathVariable String email,  @RequestHeader("Authorization") String token) {
		User user = memberService.findUserByEmail(email,token);
		if (user != null) {
			return user;
		} else {
			throw new UserNotFoundException("Email: " + email);
		}
	}

	@GetMapping("/username/{username}")
	public User getUserByUsername(@PathVariable String username,  @RequestHeader("Authorization") String token) {
		User user = memberService.findUserByUsername(username, token);
		if (user != null) {
			return user;
		} else {
			throw new UserNotFoundException("Username: " + username);
		}
	}
	
	@PutMapping("/{userId}")
	public User updateUserDetailsById(@PathVariable long userId, @RequestBody User userDetails, @RequestHeader("Authorization") String token) {
		userDetails.setId(userId);
		User updatedUser = memberService.updateUserDetailsById(userDetails,token);
		return updatedUser;
	}

	@DeleteMapping("/{userIdToDelete}")
	public String deleteUser(@PathVariable long userIdToDelete, @RequestHeader("Authorization") String token) {
		// remove the first part of the string which is "Bearer " 
		String jwtToken = token.replace("Bearer ", "").trim();

		try {
			memberService.deleteUser(userIdToDelete, jwtToken);
			return "SUCCESS: User has been deleted successfully";
		}catch (UserNotFoundException ex) {
			return ex.getMessage();
		}catch (Exception ex) {
			return ex.getMessage();
		}

	}
	

}