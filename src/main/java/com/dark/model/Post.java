package com.dark.model;

import java.time.LocalDateTime;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Post {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer postId;
	
	private String caption;
	
	private String imageURL;
	
	private String videoURL;
	
	@ManyToOne
	@JoinColumn(name = "createdByUser", referencedColumnName = "userId")
	private User user;
	
//	@JsonIgnore
	@ManyToMany
	private Set<User> likedUsers = new HashSet<>();
	
	private LocalDateTime createdAt;

	public Post() {}

	public Post(Integer postId, String caption, String imageURL, String videoURL, User user) {
		super();
		this.postId = postId;
		this.caption = caption;
		this.imageURL = imageURL;
		this.videoURL = videoURL;
		this.user = user;
		this.createdAt = LocalDateTime.now();
	}
	
	public Integer getPostId() {
		return postId;
	}
	public void setPostId(int postId) {
		this.postId = postId;
	}

	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getVideoURL() {
		return videoURL;
	}
	public void setVideoURL(String videoURL) {
		this.videoURL = videoURL;
	}

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	public Set<User> getLikedUsers() {
		return likedUsers;
	}
	public void setLikedUsers(Set<User> likedUsers) {
		this.likedUsers = likedUsers;
	}

	@Override
	public String toString() {
		return "Post [postId=" + postId + ", caption=" + caption + ", imageURL=" + imageURL + ", videoURL=" + videoURL
				+ ", user=" + user + ", createdAt=" + createdAt + "]";
	}
}