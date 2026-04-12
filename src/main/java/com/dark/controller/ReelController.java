package com.dark.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.dark.model.Reels;
import com.dark.service.Reels.ReelService;
import com.dark.service.Users.UserService;
import com.dark.Execptions.UserException;

@RestController
public class ReelController {

	@Autowired
	ReelService reelService;

	@Autowired
	UserService userService;

	@PostMapping("/api/reel")
	public ResponseEntity<Reels> createReel(@RequestBody Reels reel,
			@RequestHeader("Authorization") String jwt) {
		return new ResponseEntity<>(reelService.createReel(reel, userService.findUserByJwt(jwt)), HttpStatus.CREATED);
	}

	@GetMapping("/api/reels")
	public ResponseEntity<Page<Reels>> findAllReels(
			@PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
		return new ResponseEntity<>(reelService.findAllReels(pageable), HttpStatus.OK);
	}

	@GetMapping("/api/reels/{userId}")
	public ResponseEntity<List<Reels>> findUserReels(@PathVariable Integer userId) throws UserException {
		return new ResponseEntity<>(reelService.findUserReels(userId), HttpStatus.OK);
	}
}
