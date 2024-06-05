package com.sparta.cafe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

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
}
