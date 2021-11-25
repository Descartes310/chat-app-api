package com.chat.api.services;

import java.util.List;

import com.chat.api.repositories.entities.Chat;
import com.chat.api.repositories.entities.User;

public interface ChatService {

	Chat save(Chat chat);
	
	Chat getOne(Long id);
	
	List<Chat> findByUsers(User user);
}
