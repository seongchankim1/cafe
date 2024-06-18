package com.sparta.cafe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.sparta.cafe.service.UserService;

@Controller
public class HomeController {

	private final UserService userService;

	public HomeController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/")
	public String getIndexPage() {
		return "index"; // index.html 파일의 이름
	}

	@GetMapping("/delete")
	public String getDeletePage() {
		return "delete"; // delete.html 파일의 이름
	}

	@GetMapping("/order")
	public String getOrderPage() {
		return "order";
	}

	@GetMapping("/user/login")
	public String login() {
		return "login";
	}




}
