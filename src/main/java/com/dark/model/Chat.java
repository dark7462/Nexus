package com.dark.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Chat {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String chat_name;

	private String chat_image;

	@ManyToMany
	private List<User> users = new ArrayList<>();

	private LocalDateTime timeStamp;
	
	@OneToMany(mappedBy = "chat")
	private List<Message> messages = new ArrayList<>();
	
	
}