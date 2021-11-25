package com.chat.api.repositories.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="messages")
public class Message implements Serializable{
	
	public enum MessageType {
		TEXT,
		FILE
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3057105032739464119L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_id")
	@SequenceGenerator(name = "message_id", sequenceName = "message_id", allocationSize = 1)
	private Long id;
	
	@Column(nullable = true, columnDefinition = "TEXT")
	private String content;
	
	@Column(nullable=true)
	private String file;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private MessageType messageType;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="chat_id", nullable=false)
	private Chat chat;	
	
	@ManyToOne
	@JoinColumn(name="sender_id", nullable=false)
	private User sender;	
	
	@Column(unique = false, nullable = false)
	private Timestamp createdAt;

	public Message() {
		super();
		this.createdAt = new Timestamp(System.currentTimeMillis());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public Chat getChat() {
		return chat;
	}

	public void setChat(Chat chat) {
		this.chat = chat;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}
	
    @Override
    public String toString() {
        return "Message{" +
                "sender='" + sender.getFullName() + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

}
