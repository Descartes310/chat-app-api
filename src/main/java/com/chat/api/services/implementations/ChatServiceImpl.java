package com.chat.api.services.implementations;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chat.api.repositories.ChatRepository;
import com.chat.api.repositories.entities.Chat;
import com.chat.api.repositories.entities.User;
import com.chat.api.services.ChatService;

@Service
public class ChatServiceImpl implements ChatService {
	
	private ChatRepository chatRepository;

	public ChatServiceImpl(ChatRepository chatRepository) {
		super();
		this.chatRepository = chatRepository;
	}

	@Override
	public Chat save(Chat chat) {
		return this.chatRepository.save(chat);
	}

	@Override
	public Chat getOne(Long id) {
		return this.chatRepository.getOne(id);
	}

	@Override
	public List<Chat> findByUsers(User user) {
		return this.chatRepository.findByUsers(user);
	}

	@Override
	public List<Chat> findByUsers(List<User> users) {
		return this.chatRepository.findByUsers(users);
	}
	
}
