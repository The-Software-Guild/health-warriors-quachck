package com.wileyedge.healthyrecipe.cleanup;

import java.util.List;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wileyedge.healthyrecipe.dao.UserRepository;
import com.wileyedge.healthyrecipe.model.User;

@Component
public class TokenCleanup {

    private UserRepository userRepository;

    @Autowired
    public TokenCleanup(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PreDestroy
    public void cleanUp() {
        // Retrieve all users from the database
        List<User> users = userRepository.findAll();

        // Remove tokens from each user
        for (User user : users) {
            user.setToken(null);
        }

        // Save the modified users without tokens
        userRepository.saveAll(users);
    }
}
