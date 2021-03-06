package com.chat.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chat.api.repositories.entities.Chat;
import com.chat.api.repositories.entities.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>{

	List<Message> findByChatOrderByIdAsc(Chat chat);
}
