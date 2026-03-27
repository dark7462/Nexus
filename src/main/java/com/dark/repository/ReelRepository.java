package com.dark.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dark.model.Reels;

public interface ReelRepository extends JpaRepository<Reels, Integer>{
	public List<Reels> findByUserId(Integer userId);
	
	
}
