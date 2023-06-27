package com.wileyedge.healthyrecipe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wileyedge.healthyrecipe.dao.UserRepository;
import com.wileyedge.healthyrecipe.exception.InvalidTokenException;
import com.wileyedge.healthyrecipe.exception.UnauthorizedAccessException;
import com.wileyedge.healthyrecipe.exception.UserNotFoundException;
import com.wileyedge.healthyrecipe.model.User;

@Service
public class AdminServiceImpl implements IAdminService {

	private UserRepository userRepository;
	private AuthService authService;

	@Autowired
	public AdminServiceImpl(UserRepository userRepository,AuthService authService) {
		this.userRepository = userRepository;
		this.authService = authService;
	}
	
	@Override
	public void deleteUserByAdmin(long userIdToDelete, String token) {
		//Check if token is valid
		User loggedInUser = authService.isTokenValid(token);

		// Check if the user has the role "ADMIN"
		boolean loggedInUserIsAdmin = authService.isLoggedInUserHasAdminRole(loggedInUser);
		if (!loggedInUserIsAdmin) {
			throw new UnauthorizedAccessException("ACCESS Denied for action :  Delete User.");
		}

		// Check if the user to be deleted is exist in db
		Optional<User> userOptional = userRepository.findById(userIdToDelete);
		if(userOptional.isPresent()) {
			userRepository.deleteById(userIdToDelete);
		}else {
			throw new UserNotFoundException("User ID : " + userIdToDelete);
		}
	}

	@Override
	public User findUserByIdByAdmin(long userId, String token) {
	    // Check if token is valid
	    User loggedInUser = authService.isTokenValid(token);

	    // Check if the user has the role "ADMIN"
	    boolean loggedInUserIsAdmin = authService.isLoggedInUserHasAdminRole(loggedInUser);
	    if (!loggedInUserIsAdmin) {
	        throw new UnauthorizedAccessException("ACCESS Denied for action: Find user by ID.");
	    }

	    return userRepository.findById(userId)
	            .orElseThrow(() -> new UserNotFoundException("User ID: " + userId));
	}

	

	@Override
	public User findUserByEmailByAdmin(String email, String token) {

		User loggedInUser = authService.isTokenValid(token);

		// Check if the user has the role "ADMIN"
		if (!loggedInUser.getRole().equalsIgnoreCase("ADMIN")) {
			throw new InvalidTokenException("ACCESS Denied for action :  Find user by email.");
		}
		return userRepository.findByEmail(email);
	}

	@Override
	public User findUserByUsernameByAdmin(String username, String token) {
		//Check if token is valid
		User loggedInUser = authService.isTokenValid(token);

		// Check if the user has the role "ADMIN"
		boolean loggedInUserIsAdmin = authService.isLoggedInUserHasAdminRole(loggedInUser);
		if (!loggedInUserIsAdmin) {
			throw new UnauthorizedAccessException("ACCESS Denied for action :  Find user by name.");
		}

		return userRepository.findByUsername(username);
	}

	@Override
	public List<User> findAllUsersByAdmin(String token) {
		//Check if token is valid
		User loggedInUser = authService.isTokenValid(token);
		
		if(loggedInUser == null) {
			throw new InvalidTokenException("Invalid token");
		}
		
		// Check if the user has the role "ADMIN"
		boolean loggedInUserIsAdmin = authService.isLoggedInUserHasAdminRole(loggedInUser);
		if (!loggedInUserIsAdmin) {
			throw new UnauthorizedAccessException("ACCESS Denied for action : Get all users.");
		}
		
		

		return userRepository.findAll();
	}


}
