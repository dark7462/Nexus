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

@Entity
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

	private LocalDateTime createdAt;

	public Comment() {
	}

	public Comment(Integer commentId, String content, User user) {
		super();
		this.commentId = commentId;
		this.content = content;
		this.user = user;
		this.liked = new HashSet<>();
		this.createdAt = LocalDateTime.now();
	}

	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<User> getLiked() {
		return liked;
	}

	public void setLiked(Set<User> liked) {
		this.liked = liked;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

}
