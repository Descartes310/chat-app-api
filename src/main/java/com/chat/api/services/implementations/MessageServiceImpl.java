package com.chat.api.services.implementations;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chat.api.repositories.MessageRepository;
import com.chat.api.repositories.entities.Chat;
import com.chat.api.repositories.entities.Message;
import com.chat.api.services.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

	private MessageRepository messageRepository;

	public MessageServiceImpl(MessageRepository messageRepository) {
		super();
		this.messageRepository = messageRepository;
	}

	@Override
	public Message save(Message message) {
		return this.messageRepository.save(message);
	}

	@Override
	public List<Message> findByChat(Chat chat) {
		return this.messageRepository.findByChatOrderByIdAsc(chat);
	}

}
