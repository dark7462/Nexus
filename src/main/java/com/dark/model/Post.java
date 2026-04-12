package com.dark.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer postId;

	private String caption;

	private String imageURL;

	private String videoURL;

	@ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
	@JoinColumn(name = "createdByUser", referencedColumnName = "userId")
	private User user;

	// @JsonIgnore
	@ManyToMany
	private Set<User> likedUsers = new HashSet<>();

	private LocalDateTime createdAt = LocalDateTime.now();

	@OneToMany(mappedBy = "post", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();
}