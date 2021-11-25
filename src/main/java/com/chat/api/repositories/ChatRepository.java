package com.chat.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chat.api.repositories.entities.Chat;
import com.chat.api.repositories.entities.User;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long>{
	
	List<Chat> findByUsers(User user);
	
	List<Chat> findByUsersIn(List<User> users);
}
