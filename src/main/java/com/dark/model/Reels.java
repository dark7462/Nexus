package com.dark.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Reels {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String title;

	private String video;

	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;

	private LocalDateTime createdAt = LocalDateTime.now();
}
