package com.chat.api.services.implementations;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chat.api.repositories.UserRepository;
import com.chat.api.repositories.entities.User;
import com.chat.api.services.UserService;

@Service
public class UserServiceImpl implements UserService{

	private UserRepository userRepository;
	
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public User save(User user) {
		return this.userRepository.save(user);
	}

	@Override
	public User findByLogin(String login) {
		return this.userRepository.findByLogin(login);
	}

	@Override
	public List<User> findAll() {
		return this.userRepository.findAll();
	}

}
