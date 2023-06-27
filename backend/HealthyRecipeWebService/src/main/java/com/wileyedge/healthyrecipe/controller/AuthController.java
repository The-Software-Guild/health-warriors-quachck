package com.wileyedge.healthyrecipe.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wileyedge.healthyrecipe.model.LoginRequest;
import com.wileyedge.healthyrecipe.service.IAuthService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

	private IAuthService authService;

	@Autowired
	public AuthController(IAuthService authService) {
		this.authService = authService;
	}

	
	@PostMapping("/login")
	public String loginUser(@RequestBody LoginRequest loginRequest) {
	    String identifier = loginRequest.getIdentifier();
	    String password = loginRequest.getPassword();
	    
	    String token = authService.loginUser(identifier, password);
	    
	    return token;
	}
	
	@PostMapping("/logout")
	public String logoutUser(@RequestHeader("Authorization") String token) {
	    authService.logoutUser(token);
	    return "User logged out successfully.";
	}


}
