package com.dark.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer commentId;

	private String content;

	@ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
	private User user;

	@com.fasterxml.jackson.annotation.JsonIgnore
	@ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
	@jakarta.persistence.JoinColumn(name = "postId")
	private Post post;

	@ManyToMany
	private Set<User> liked = new HashSet<>();

	private LocalDateTime createdAt = LocalDateTime.now();
}
