package com.chat.api.services;

import java.util.List;

import com.chat.api.repositories.entities.User;

public interface UserService {

	User save(User user);
	
	List<User> findAll();

	User getOne(Long id);

	User findByLogin(String login);
	
}
