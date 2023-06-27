package com.wileyedge.healthyrecipe.service;

import java.util.List;

import com.wileyedge.healthyrecipe.model.User;

public interface IAdminService {

	List<User> findAllUsersByAdmin(String token);

	void deleteUserByAdmin(long userId, String token);

	User findUserByUsernameByAdmin(String username, String token);

	User findUserByEmailByAdmin(String email, String token);

	User findUserByIdByAdmin(long userId, String token);

}
