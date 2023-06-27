package com.wileyedge.healthyrecipe.service;

import com.wileyedge.healthyrecipe.model.User;

public interface IAuthService {

	boolean isLoggedInUserHasAdminRole(User loggedInUser);

	void logoutUser(String token);

	User isTokenValid(String token);

	String loginUser(String identifier, String password);

}
