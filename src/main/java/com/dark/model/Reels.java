package com.dark.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Reels {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String title;

	private String video;

	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;

	private LocalDateTime createdAt;

	public Reels(Integer id, String title, String video) {
		super();
		this.id = id;
		this.title = title;
		this.video = video;
		this.createdAt = LocalDateTime.now();
	}
}
