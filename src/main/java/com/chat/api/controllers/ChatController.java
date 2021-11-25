package com.chat.api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.chat.api.configurations.UploadService;
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
	private UploadService uploadService;
	private MessageService messageService;
	private KafkaTemplate<String, Message> kafkaTemplate;

	public ChatController(UserService userService, ChatService chatService, UploadService uploadService,
			MessageService messageService, KafkaTemplate<String, Message> kafkaTemplate) {
		this.userService = userService;
		this.chatService = chatService;
		this.uploadService = uploadService;
		this.kafkaTemplate = kafkaTemplate;
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
		
		if (user.getId().equals(interlocuter_id))
			throw new ResponseStatusException(HttpStatus.CONFLICT, "You cannot start a chat with you");

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

		return new ResponseEntity<Chat>(chat, HttpStatus.CREATED);
	}

	@PostMapping("{id}/messages")
	public void createMessage(@PathVariable Long id, @RequestParam(required = false) String content,
			@RequestParam(required = false) MultipartFile file) {

		User user = Utilities.getLoggedUser(userService);

		if (user == null)
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Logged user was not found");

		Chat chat = this.chatService.getOne(id);

		if (chat == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					String.format("The chat with id %d was not found", id));

		Message message = new Message();

		message.setChat(chat);
		message.setSender(user);
		message.setContent(content);

		if (file != null) {
			// Uploads file into the folder "files", the folder will be automatically
			// created
			message.setFile(this.uploadService.uploadFile(file, "files"));
		}

		message.setMessageType(file != null ? MessageType.FILE : MessageType.TEXT);

		message = this.messageService.save(message);
		
		try {
            //Sending the message to kafka topic queue
            this.kafkaTemplate.send(Utilities.KAFKA_TOPIC, message).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}

	@GetMapping("{id}/messages")
	public ResponseEntity<List<Message>> getMessages(@PathVariable Long id) {

		Chat chat = this.chatService.getOne(id);

		if (chat == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					String.format("The chat with id %d was not found", id));

		return new ResponseEntity<List<Message>>(this.messageService.findByChat(chat), HttpStatus.OK);

	}
	
    //    -------------- WebSocket API ----------------
    @MessageMapping("/sendMessage")
    @SendTo("/topic/group")
    public Message broadcastGroupMessage(@Payload Message message) {
        //Sending this message to all the subscribers
        return message;
    }

    @MessageMapping("/newUser")
    @SendTo("/topic/group")
    public Message addUser(@Payload Message message,
                           SimpMessageHeaderAccessor headerAccessor) {
        // Add user in web socket session
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        return message;
    }

}
