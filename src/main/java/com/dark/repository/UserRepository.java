package com.dark.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dark.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

    @Query("SELECT u FROM User u WHERE u.email = :email")
    public User findByEmail(String email);
	
	@Query("SELECT DISTINCT u FROM User u WHERE u.firstName LIKE %:query% OR u.lastName LIKE %:query% OR u.email LIKE %:query%")
	public List<User> searchUser(@Param(value = "query") String query);
	
	
}
