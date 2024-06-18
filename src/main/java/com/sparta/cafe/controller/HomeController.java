package com.sparta.cafe.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sparta.cafe.dto.SignupRequestDto;
import com.sparta.cafe.entity.User;
import com.sparta.cafe.entity.UserRoleEnum;
import com.sparta.cafe.jwt.JwtUtil;
import com.sparta.cafe.repository.UserRepository;
import com.sparta.cafe.security.UserDetailsImpl;
import com.sparta.cafe.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class HomeController {

	private final UserService userService;
	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;

	public HomeController(UserService userService, JwtUtil jwtUtil, UserRepository userRepository) {
		this.userService = userService;
		this.jwtUtil = jwtUtil;
		this.userRepository = userRepository;
	}

	@GetMapping("/display")
	public String getDisplayPage() {
		return "index";
	}

	@GetMapping("/home")
	public void getLoginOrOrderPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String token = jwtUtil.resolveTokenFromCookies(request);
		if (jwtUtil.validateToken(token)) {
			response.sendRedirect("/order");
		} else {
			response.sendRedirect("/user/login-page");
		}
	}

	@GetMapping("/delete")
	public String getDeletePage(HttpServletRequest request) {
		String token = jwtUtil.resolveTokenFromCookies(request);
		User user = userRepository.findByUsername(jwtUtil.getUserInfoFromToken(token).getSubject());
		if (user.getRole() == UserRoleEnum.ADMIN || user.getRole() == UserRoleEnum.STAFF) {
			return "delete";
		} else {
			return "order";
		}
	}

	@GetMapping("/order")
	public String getOrderPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
			return "order";
	}

	@GetMapping("/user/login-page")
	public String loginPage() {
		return "login";
	}

	@PostMapping("/user/signup")
	public void signup(@RequestBody SignupRequestDto requestDto, HttpServletResponse response) throws IOException {
		boolean isSignupSuccessful = userService.signup(requestDto);

		if (isSignupSuccessful) {
			// 회원가입 성공 시 로그인 페이지로 리디렉션
			response.sendRedirect("/user/login-page");
		} else {
			// 회원가입 실패 시 적절한 처리
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "회원가입 실패");
		}
	}


}
