package com.dark.service.Reels;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dark.model.Reels;
import com.dark.model.User;
import com.dark.repository.ReelRepository;
import com.dark.service.UserService;

@Service
public class ReelServiceImplimentation implements ReelService {

	@Autowired
	ReelRepository reelRepository;

	@Autowired
	UserService userService;

	@Override
	public Reels createReel(Reels reel, User user) {
		reel.setUser(user);
		return reelRepository.save(reel);
	}

	@Override
	public List<Reels> findAllReels() {
		return reelRepository.findAll();
	}

	@Override
	public List<Reels> findUserReels(Integer userId) throws Exception {
		if (userService.findById(userId) == null) {
			throw new Exception("User doesn't exsits..!!");
		}
		return reelRepository.findByUserId(userId);
	}

}
