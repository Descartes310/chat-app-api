package com.chat.api.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.chat.api.configurations.Utilities;
import com.chat.api.repositories.entities.User;
import com.chat.api.services.ChatService;
import com.chat.api.services.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("api/users")
@Api(tags = "User actions")
public class UserController {

	private UserService userService;
	private ChatService chatService;
	private DefaultTokenServices defaultTokenServices;

	public UserController(UserService userService, ChatService chatService, DefaultTokenServices defaultTokenServices) {
		this.userService = userService;
		this.chatService = chatService;
		this.defaultTokenServices = defaultTokenServices;
	}

	@GetMapping("kyc")
	@ApiOperation("Get informations about logged user")
	public ResponseEntity<User> getLoggedUser() {

		User user = Utilities.getLoggedUser(userService);

		if (user == null)
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Logged user was not found");

		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<User>> getUsers() {
		
		User user = Utilities.getLoggedUser(userService);

		if (user == null)
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Logged user was not found");
		
		//We only return users with no chat with logged user
		
		List<User> interlocutors = this.chatService.findByUsers(user).stream().map(c -> c.getUsers()).flatMap(List::stream).collect(Collectors.toList());
		
		return new ResponseEntity<List<User>>(this.userService.findAll().stream().filter(u -> !interlocutors.contains(u)).collect(Collectors.toList()), HttpStatus.OK);
	}

	@DeleteMapping("logout")
	@ApiOperation("Logout connected user")
	public void revokeToken(HttpServletRequest request) {
		
		String authorization = request.getHeader("Authorization");
		if (authorization != null && authorization.contains("Bearer")) {
			String tokenId = authorization.substring("Bearer".length() + 1);
			this.defaultTokenServices.revokeToken(tokenId);
		}
	}

}
