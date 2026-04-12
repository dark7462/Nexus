package com.dark.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
@jakarta.persistence.Table(name = "users")
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer userId;

	@Column(nullable = false)
	private String firstName;

	private String lastName;

	@Column(nullable = false)
	private String gender;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	@ElementCollection
	private Set<Integer> followers = new HashSet<>();

	@ElementCollection
	private Set<Integer> following = new HashSet<>();

	@JsonIgnore // so it doesn't appear in postman recursively
	@ManyToMany
	private Set<Post> savedPosts = new HashSet<>();

	public User(Integer id, String firstName, String lastName, String gender, String email, String password) {
		super();
		this.userId = id;
		this.firstName = firstName.toLowerCase();
		this.lastName = lastName.toLowerCase();
		this.gender = gender.toLowerCase();
		this.email = email.toLowerCase();
		this.password = password;
	}

	public Integer getId() {
		return userId;
	}

	public void setId(int id) {
		this.userId = id;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName.toLowerCase();
	}

	public void setLastName(String lastName) {
		this.lastName = lastName.toLowerCase();
	}

	public void setGender(String gender) {
		this.gender = gender.toLowerCase();
	}

	public void setEmail(String email) {
		this.email = email.toLowerCase();
	}
}