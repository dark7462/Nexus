package com.dark.service.Reels;

import java.util.List;

import com.dark.model.Reels;
import com.dark.model.User;

public interface ReelService {
	
	public Reels createReel(Reels reel, User user);
	
	public List<Reels> findAllReels();
	
	public List<Reels> findUserReels(Integer userId) throws Exception;
}
