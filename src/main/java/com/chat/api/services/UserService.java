package com.chat.api.services;

import com.chat.api.repositories.entities.User;

public interface UserService {

	User save(User user);
	
	User findByLogin(String login);
}
