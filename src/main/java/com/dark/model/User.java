package com.dark.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
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
	private String password;
	
	@ElementCollection
	private Set<Integer> followers = new HashSet<>();
	
	@ElementCollection
	private Set<Integer> following = new HashSet<>();
	
	@JsonIgnore // so it doesn't appear in postman recursively
	@ManyToMany
	private Set<Post> savedPosts = new HashSet<>();

	public User() {}

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

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName.toLowerCase(); 
	}
	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName.toLowerCase(); 
	}
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender.toLowerCase();
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email.toLowerCase();
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Integer> getFollowers() {
		return this.followers;
	}
	public void setFollowers(Set<Integer> followers) {
		this.followers = followers;
	}

	public Set<Integer> getFollowing() {
		return this.following;
	}
	public void setFollowing(Set<Integer> following) {
		this.following = following;
	}

	public Set<Post> getSavedPosts() {
		return savedPosts;
	}
	public void setSavedPosts(Set<Post> savedPosts) {
		this.savedPosts = savedPosts;
	}

	@Override
	public String toString() {
		return "User [id=" + userId + ", firstName=" + firstName + ", lastName=" + lastName + ", gender=" + gender
				+ ", email=" + email + ", password=" + password + ", followers=" + followers + ", following="
				+ following + "]";
	}
}