package com.wileyedge.healthyrecipe.service;

import com.wileyedge.healthyrecipe.model.User;

public class AuthServiceMock implements IAuthService {

	@Override
	public User isTokenValid(String token) {
		return new User("john.doe", "john.doe@example.com", "password123", "John", "Doe");
	}

	@Override
	public boolean isLoggedInUserHasAdminRole(User loggedInUser) {
		return false;
	}

	@Override
	public void logoutUser(String token) {
	}

	@Override
	public String loginUser(String identifier, String password) {
		return null;
	}

}
