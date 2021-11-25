package com.chat.api.repositories.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="chats")
public class Chat implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5497843188255232116L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_id")
	@SequenceGenerator(name = "chat_id", sequenceName = "chat_id", allocationSize = 1)
	private Long id;
	
	@ManyToMany
	@JoinTable(
			name = "chat_users", 
			joinColumns = @JoinColumn(name = "chat_id"), 
			inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<User> users = new ArrayList<User>();
	
	@Column(unique = false, nullable = false)
	private Timestamp createdAt;

	@Column(unique = false, nullable = false)
	private Timestamp updatedAt;

	public Chat() {
		super();
		this.createdAt = new Timestamp(System.currentTimeMillis());
		this.updatedAt = new Timestamp(System.currentTimeMillis());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
	
}
