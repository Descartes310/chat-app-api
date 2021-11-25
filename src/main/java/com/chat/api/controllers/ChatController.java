package com.chat.api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.chat.api.configurations.Utilities;
import com.chat.api.repositories.entities.Chat;
import com.chat.api.repositories.entities.Message;
import com.chat.api.repositories.entities.Message.MessageType;
import com.chat.api.repositories.entities.User;
import com.chat.api.services.ChatService;
import com.chat.api.services.MessageService;
import com.chat.api.services.UserService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("api/chats")
@Api(tags = "Chat actions")
public class ChatController {

	private UserService userService;
	private ChatService chatService;
	private MessageService messageService;

	public ChatController(UserService userService, ChatService chatService, MessageService messageService) {
		this.userService = userService;
		this.chatService = chatService;
		this.messageService = messageService;
	}

	@GetMapping
	public ResponseEntity<List<Chat>> getChat() {

		User user = Utilities.getLoggedUser(userService);

		if (user == null)
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Logged user was not found");

		return new ResponseEntity<List<Chat>>(this.chatService.findByUsers(user), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Chat> createChat(@RequestParam Long interlocuter_id) {

		User user = Utilities.getLoggedUser(userService);

		if (user == null)
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Logged user was not found");

		User interlocuter = this.userService.getOne(interlocuter_id);

		if (interlocuter == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					String.format("The user with id %d was not found", interlocuter_id));

		// Checking if the chat already exists
		List<Chat> chats = this.chatService.findByUsers(List.of(user, interlocuter));
		if (chats.size() > 0)
			return new ResponseEntity<Chat>(chats.get(0), HttpStatus.OK);

		Chat chat = new Chat();

		chat.setUsers(List.of(user, interlocuter));

		chat = this.chatService.save(chat);

		return new ResponseEntity<Chat>(chat, HttpStatus.OK);
	}

	@PostMapping("{id}/messages")
	public ResponseEntity<Message> createMessage(@PathVariable Long id, @RequestParam String content, @RequestParam MultipartFile file) {

		User user = Utilities.getLoggedUser(userService);

		if (user == null)
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Logged user was not found");

		Chat chat = this.chatService.getOne(id);

		if (chat == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					String.format("The chat with id %d was not found", id));

		Message message = new Message();

		message.setFile(file);
		message.setChat(chat);
		message.setSender(user);
		message.setContent(content);
		message.setMessageType(file != null ? MessageType.FILE : MessageType.TEXT);

		Chat chat = new Chat();

		chat.setUsers(List.of(user, interlocuter));

		chat = this.chatService.save(chat);

		return new ResponseEntity<Chat>(chat, HttpStatus.OK);
	}

}
