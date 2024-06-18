package com.sparta.cafe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.cafe.dto.UserDto;
import com.sparta.cafe.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/api/user/info")
	public UserDto getUserInfo(HttpServletRequest request) {
		String username = userService.getUsernameFromToken(request);
		return userService.getUserInfo(username);
	}
}
