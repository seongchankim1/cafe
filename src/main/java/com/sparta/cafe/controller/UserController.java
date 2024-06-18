package com.sparta.cafe.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.cafe.dto.LoginRequestDto;
import com.sparta.cafe.dto.SignupRequestDto;
import com.sparta.cafe.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/user/signup")
	public void signup(@RequestBody SignupRequestDto requestDto, HttpServletResponse response) throws IOException {
		boolean isSignupSuccessful = userService.signup(requestDto);

		if (isSignupSuccessful) {
			// 회원가입 성공 시 로그인 페이지로 리디렉션
			response.sendRedirect("/user/login");
		} else {
			// 회원가입 실패 시 적절한 처리
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "회원가입 실패");
		}
	}

	@PostMapping("/user/login")
	public void login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response) throws IOException {
		boolean isLoginSuccessful = userService.login(requestDto, response);

		if (isLoginSuccessful) {
			// 로그인 성공 시 주문 페이지로 리디렉션
			response.sendRedirect("/user/order");
		} else {
			// 로그인 실패 시 적절한 처리
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "로그인 실패");
		}
	}
}
