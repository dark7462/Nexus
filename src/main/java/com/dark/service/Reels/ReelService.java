package com.dark.service.Reels;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dark.model.Reels;
import com.dark.model.User;
import com.dark.Exceptions.UserException;

public interface ReelService {
	
	public Reels createReel(Reels reel, User user);
	
	public List<Reels> findAllReels();

	public Page<Reels> findAllReels(Pageable pageable);
	
	public List<Reels> findUserReels(Integer userId) throws UserException;
}
