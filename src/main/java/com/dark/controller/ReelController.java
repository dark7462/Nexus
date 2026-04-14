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
import com.dark.response.ReelDto;
import com.dark.request.CreateReelRequest;
import com.dark.mapper.DtoMapper;
import com.dark.service.Reels.ReelService;
import com.dark.service.Users.UserService;
import com.dark.Exceptions.UserException;
import jakarta.validation.Valid;
import java.util.stream.Collectors;

@RestController
public class ReelController {

	@Autowired
	ReelService reelService;

	@Autowired
	UserService userService;

	@PostMapping("/api/reel")
	public ResponseEntity<ReelDto> createReel(@Valid @RequestBody CreateReelRequest req,
			@RequestHeader("Authorization") String jwt) {
		Reels reel = new Reels();
		reel.setTitle(req.getTitle());
		reel.setVideo(req.getVideo());
		return new ResponseEntity<>(DtoMapper.toReelDto(reelService.createReel(reel, userService.findUserByJwt(jwt))), HttpStatus.CREATED);
	}

	@GetMapping("/api/reels")
	public ResponseEntity<Page<ReelDto>> findAllReels(
			@PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
		Page<ReelDto> reels = reelService.findAllReels(pageable).map(DtoMapper::toReelDto);
		return new ResponseEntity<>(reels, HttpStatus.OK);
	}

	@GetMapping("/api/reels/{userId}")
	public ResponseEntity<List<ReelDto>> findUserReels(@PathVariable Integer userId) throws UserException {
		List<ReelDto> reels = reelService.findUserReels(userId).stream().map(DtoMapper::toReelDto).collect(Collectors.toList());
		return new ResponseEntity<>(reels, HttpStatus.OK);
	}
}
