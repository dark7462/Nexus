package com.dark.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.dark.model.Reels;
import com.dark.service.Reels.ReelService;
import com.dark.service.Users.UserService;

@RestController
public class ReelController {

	@Autowired
	ReelService reelService;

	@Autowired
	UserService userService;

	@PostMapping("/api/reel")
	public Reels createReel(@RequestBody Reels reel, @RequestHeader("Authorization") String jwt) {
		return reelService.createReel(reel, userService.findUserByJwt(jwt));
	}

	@GetMapping("/api/reels")
	public List<Reels> findAllReels() {
		return reelService.findAllReels();
	}

	@GetMapping("/api/reels/{userId}")
	public List<Reels> findUserReels(@PathVariable Integer userId) throws Exception {
		return reelService.findUserReels(userId);
	}
}
