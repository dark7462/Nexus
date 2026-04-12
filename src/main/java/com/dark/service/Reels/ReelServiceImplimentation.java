package com.dark.service.Reels;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dark.model.Reels;
import com.dark.model.User;
import com.dark.repository.ReelRepository;
import com.dark.service.Users.UserService;
import com.dark.Exceptions.UserException;

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
	public Page<Reels> findAllReels(Pageable pageable) {
		return reelRepository.findAll(pageable);
	}

	@Override
	public List<Reels> findUserReels(Integer userId) throws UserException {
		if (userService.findById(userId) == null) {
			throw new UserException("User doesn't exsits..!!");
		}
		return reelRepository.findByUserId(userId);
	}

}
