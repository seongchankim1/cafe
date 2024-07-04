package com.sparta.cafe.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.cafe.dto.CompleteOrderResponseDto;
import com.sparta.cafe.dto.MoneyRequestDto;
import com.sparta.cafe.dto.OrderResponseDto;
import com.sparta.cafe.dto.UserResponseDto;
import com.sparta.cafe.service.OrderService;
import com.sparta.cafe.service.UserService;

@RestController
@RequestMapping("/api/database")
public class DBController {

	private final OrderService orderService;
	private final UserService userService;

	public DBController(OrderService orderService, UserService userService) {
		this.orderService = orderService;
		this.userService = userService;
	}

	@GetMapping("/complete")
	public List<CompleteOrderResponseDto> getCompleteOrderList() {
		return orderService.getCompleteOrdersDB();
	}

	@GetMapping("/user")
	public List<UserResponseDto> getUserList() {
		return userService.getUserDB();
	}

	@PostMapping("/money")
	public UserResponseDto addMoney(@RequestBody MoneyRequestDto request) {
		return userService.addMoney(request);
	}
}
