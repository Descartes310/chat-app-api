package com.chat.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.chat.api.configurations.Utilities;
import com.chat.api.repositories.entities.User;
import com.chat.api.services.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("auth")
@Api(tags = "Authentication actions")
public class AuthController {

	private UserService userService;
	private DefaultTokenServices defaultTokenServices;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public AuthController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder,
			DefaultTokenServices defaultTokenServices) {
		this.userService = userService;
		this.defaultTokenServices = defaultTokenServices;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@GetMapping("/test")
	public String test() {
		return "Chat API is working";
	}

	@PostMapping("register")
	@ApiOperation("Create user account")
	public ResponseEntity<OAuth2AccessToken> userRegistration(@RequestParam String fullName, @RequestParam String login,
			@RequestParam String password) {

		User user = new User();
		
		if(this.userService.findByLogin(login) != null) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("The user with login %s already exists.", login));
		}

		user.setAvatar(null);
		user.setLogin(login);
		user.setFullName(fullName);
		user.setPassword(this.bCryptPasswordEncoder.encode(password));
		
		user = this.userService.save(user);

		// Generating access token for created user
		OAuth2AccessToken userToken = Utilities.createAccessToken(this.defaultTokenServices, user);

		return new ResponseEntity<OAuth2AccessToken>(userToken, HttpStatus.CREATED);
	}

}
