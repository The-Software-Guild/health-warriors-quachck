package com.wileyedge.healthyrecipe.Utilities;



import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wileyedge.healthyrecipe.dao.UserRepository;
import com.wileyedge.healthyrecipe.exception.InvalidTokenException;
import com.wileyedge.healthyrecipe.exception.UserNotFoundException;
import com.wileyedge.healthyrecipe.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenUtils {
	private UserRepository userRepository;
	private Key key;
	
	@Autowired
    public TokenUtils(UserRepository userRepository) {
		this.userRepository = userRepository;
		this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}



	public String generateToken(User user) {
	    String role = user.getRole();
	    long userId = user.getId();

	    // Generate the token
	    String token = Jwts.builder()
	            .setSubject(user.getUsername())
	            .claim("role", role)
	            .claim("userId", userId)
	            .setIssuedAt(new Date())
	            .setExpiration(new Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000))   // Token validity: 2 hours
	            .signWith(key, SignatureAlgorithm.HS256)
	            .compact();

	    return token;
	}
    

	//validate token
	  public boolean checkIfJwtToken(String token) {		  
		  String cleanedToken = token.replace("Bearer ", "");
		    
	        try {
	            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(cleanedToken);         	            
	            return true;
	        } catch (ExpiredJwtException ex) {
	        	throw new InvalidTokenException("Token has expired. Please login again.");
	        } catch (JwtException ex) {
	        	throw new InvalidTokenException("Invalid Jwt Token");
	        }
	    }
	
	
    public User getUserFromToken(String token) {    
    	try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();

            // Retrieve the user from the database based on the username
            User user = userRepository.findByUsername(username);
            if (user == null) {
                throw new UserNotFoundException("username : " + username);
            }        
            return user;
        } catch (ExpiredJwtException ex) {
            throw new InvalidTokenException("Token has expired. Please login again.");
        } catch (JwtException ex) {
            throw new InvalidTokenException("Invalid Jwt Token.");
        }

    }
        

}
