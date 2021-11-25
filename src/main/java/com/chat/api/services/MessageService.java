package com.chat.api.services;

import java.util.List;

import com.chat.api.repositories.entities.Chat;
import com.chat.api.repositories.entities.Message;

public interface MessageService {

	Message save(Message message);
	
	List<Message> findByChat(Chat chat);
	
}
