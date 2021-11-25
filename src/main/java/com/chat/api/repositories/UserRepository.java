package com.chat.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chat.api.repositories.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByLogin(String login);
}
