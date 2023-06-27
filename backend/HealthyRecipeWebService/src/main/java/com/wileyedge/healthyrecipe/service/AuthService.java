package com.wileyedge.healthyrecipe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.wileyedge.healthyrecipe.Utilities.TokenUtils;
import com.wileyedge.healthyrecipe.dao.UserRepository;
import com.wileyedge.healthyrecipe.exception.InvalidCredentialException;
import com.wileyedge.healthyrecipe.exception.InvalidTokenException;
import com.wileyedge.healthyrecipe.model.User;


@Service
public class AuthService implements IAuthService {
	private UserRepository userRepository;
	private TokenUtils tokenUtils;
	private BCryptPasswordEncoder passwordEncoder;

	public AuthService() {
		super();
	}

	@Autowired
	public AuthService(UserRepository userRepository,TokenUtils tokenUtils,BCryptPasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.tokenUtils = tokenUtils;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public String loginUser(String identifier, String password) {
	    boolean isEmail = identifier.contains("@") && identifier.contains(".");
	    User user = isEmail ? userRepository.findByEmail(identifier) : userRepository.findByUsername(identifier);

	    if (user == null) {
	        throw new InvalidCredentialException("Incorrect username or email.");
	    } else if (!passwordEncoder.matches(password, user.getPassword())) {
	        throw new InvalidCredentialException("Incorrect password.");
	    } else {
	        String token = tokenUtils.generateToken(user);

	        // Clean the token to remove "Bearer " prefix
	        String cleanedToken = token.replace("Bearer ", "");

	        // Hash the cleaned token using bcrypt
	        String hashedToken = BCrypt.hashpw(cleanedToken, BCrypt.gensalt());

	        user.setToken(hashedToken);
	        userRepository.save(user);
	        return token;
	    }
	}

	
	@Override
	public User isTokenValid(String token) {
	    // Clean the token by removing the "Bearer " prefix
	    String cleanedToken = token.replace("Bearer ", "");

	    try {
	        // Validates the integrity and authenticity of the token based on its signature
	        tokenUtils.checkIfJwtToken(cleanedToken);

	        // Validate user embedded in the token actually exists
	        User loggedInUser = tokenUtils.getUserFromToken(cleanedToken);

	        // Retrieve token from DB basedOn LoggedInUser info
	        String storedToken = userRepository.findById(loggedInUser.getId()).orElse(null).getToken();

	        // Check if the provided token matches the user's stored hashed token
	        if (storedToken != null && BCrypt.checkpw(cleanedToken, storedToken)) {
	            return loggedInUser;
	        } else {
	            throw new InvalidTokenException("Invalid token");
	        }
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	    }

	    return null;
	}


	
	@Override
	public void logoutUser(String token) {
		// Extract the token from the Authorization header
		String authToken = token.replace("Bearer ", "");

		// Validate and decode the token to retrieve the user details
		User user = tokenUtils.getUserFromToken(authToken);

		user.setToken("");
		userRepository.save(user);
	}

	@Override
	public boolean isLoggedInUserHasAdminRole(User loggedInUser) {		
		if (loggedInUser.getRole().equalsIgnoreCase("ADMIN")) {
			return true;
		}else {
			return false;
		}

	}

}
