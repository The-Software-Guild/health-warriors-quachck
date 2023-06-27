package com.wileyedge.healthyrecipe.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wileyedge.healthyrecipe.exception.InvalidTokenException;
import com.wileyedge.healthyrecipe.exception.UserNotFoundException;
import com.wileyedge.healthyrecipe.model.User;
import com.wileyedge.healthyrecipe.service.IAdminService;


@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {

	private IAdminService adminService;

	public AdminController(IAdminService adminService) {
		this.adminService = adminService;
	}

	@GetMapping("/users")
	public List<User> getAllUsersByAdmin(@RequestHeader("Authorization") String token) {
		List<User> users = null;

		if(token == null || token.isBlank() || token.isEmpty()) {
			throw new InvalidTokenException("no token found");
		}
		// remove the first part of the string which is "Bearer " 
		String jwtToken = token.replace("Bearer ", "").trim();
		users = adminService.findAllUsersByAdmin(jwtToken);			

		return users;
	}


	@GetMapping("/users/{userId}")
	public User getUserByIdByAdmin(@PathVariable Integer userId,  @RequestHeader("Authorization") String token) {
		User user =  adminService.findUserByIdByAdmin(userId, token);
		return user;
	}
	

	@GetMapping("/users/email/{email}")
	public User getUserByEmailByAdmin(@PathVariable String email,  @RequestHeader("Authorization") String token) {
		User user = adminService.findUserByEmailByAdmin(email,token);
		if (user != null) {
			return user;
		} else {
			throw new UserNotFoundException("Email: " + email);
		}
	}

	@GetMapping("/users/username/{username}")
	public User getUserByUsernameByAdmin(@PathVariable String username,  @RequestHeader("Authorization") String token) {
		User user = adminService.findUserByUsernameByAdmin(username, token);
		if (user != null) {
			return user;
		} else {
			throw new UserNotFoundException("Username: " + username);
		}
	}

	@DeleteMapping("/users/{userIdToDelete}")
	public String deleteUserByAdmin(@PathVariable long userIdToDelete, @RequestHeader("Authorization") String token) {
		// remove the first part of the string which is "Bearer " 
		String jwtToken = token.replace("Bearer ", "").trim();

		try {
			adminService.deleteUserByAdmin(userIdToDelete, jwtToken);
			return "SUCCESS: User has been deleted successfully";
		}catch (UserNotFoundException ex) {
			return ex.getMessage();
		}catch (Exception ex) {
			return ex.getMessage();
		}

	}

}
